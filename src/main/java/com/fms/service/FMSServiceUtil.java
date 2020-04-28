package com.fms.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fms.model.EmailData;
import com.fms.model.EventDetail;
import com.fms.model.Events;
import com.fms.model.FeedbackQuestion;
import com.fms.model.ParticipatedDetails;
import com.fms.model.User;
import com.fms.model.VolunteerDetails;
import com.fms.model.pocDetails;
import com.fms.repository.EventDetailsRepository;
import com.fms.repository.EventRepository;
import com.fms.repository.FeedbackRepository;
import com.fms.repository.UserInfoRepository;
import com.fms.repository.VolunteerDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FMSServiceUtil {

	@Autowired
	UserInfoRepository userRepo;

	@Autowired
	EventDetailsRepository eventDetailRepo;

	@Autowired
	EventRepository eventRepo;

	@Autowired
	FeedbackRepository feedbackRepo;

	@Autowired
	VolunteerDetailsRepository volunteerRepo;

	@Autowired
	EmailService emailService;

	@Value("#{'${headers.name}'.split(',')}")
	private List<String> headers;

	@Value("${email.bu.fileName}")
	private String fileName;

	public String sendEmailToBU(EmailData emailData) throws Exception {
		FileOutputStream outStream = new FileOutputStream(fileName);
		generateExcelFile(emailData.getEvents(), outStream);
		return emailService.sendEmail(emailData.getMailId(), fileName);
	}

	private void generateExcelFile(List<String> eventIds, FileOutputStream outStream)
			throws FileNotFoundException, IOException {
		List<Events> eventList = eventRepo.findByeventIdIn(eventIds);

		List<EventDetail> eventDetailsList = eventDetailRepo.findByeventIdIn(eventIds);
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			CreationHelper createHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.createSheet("Event Detail");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < headers.size(); col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headers.get(col));
				cell.setCellStyle(headerCellStyle);
			}

			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			int rowIdx = 1;
			for (Events event : eventList) {
				Row row = sheet.createRow(rowIdx++);
				EventDetail eDetail = eventDetailsList.stream().filter(i -> i.getEventId().equals(event.getEventId()))
						.findFirst().orElse(null);
				pocDetails pocDetail = event.getPocDetail();

				row.createCell(0).setCellValue(event.getEventId());
				row.createCell(1).setCellValue(eDetail.getBaseLocation());
				row.createCell(2).setCellValue(eDetail.getBeneficiaryName());
				row.createCell(3).setCellValue(eDetail.getCouncilName());
				row.createCell(4).setCellValue(eDetail.getEventName());
				row.createCell(5).setCellValue(eDetail.getEventDescription());
				row.createCell(6).setCellValue(event.getMonth());
				row.createCell(7).setCellValue(event.getVenueAddress());
				row.createCell(8).setCellValue(event.getProject());
				row.createCell(9).setCellValue(event.getCategory());
				Cell ageCell = row.createCell(10);
				ageCell.setCellValue(event.getEventDate().toString());
				ageCell.setCellStyle(ageCellStyle);

				row.createCell(11).setCellValue(event.getTotalVolunteers());
				row.createCell(12).setCellValue(event.getTotalVolunteersHours());

				row.createCell(13).setCellValue(event.getTotalTravelHours());
				row.createCell(14).setCellValue(event.getLivesImpacted());
				row.createCell(15).setCellValue(event.getOverallVolunteerHours());
				row.createCell(16).setCellValue(event.getActivityType());
				row.createCell(17).setCellValue(event.getStatus());
				row.createCell(18).setCellValue(pocDetail.getPocId());
				row.createCell(19).setCellValue(pocDetail.getPocName());
				row.createCell(20).setCellValue(pocDetail.getPocNumber());
			}

			workbook.write(out);
			out.writeTo(outStream);

		}

		catch (Exception e) {
			log.error("Exception while generating File {}", e.getMessage());
		} finally {
			outStream.close();
			workbook.close();
		}

	}

	public void sendEmailToParticipant() {
		String role = "participant";
		try {
			List<User> users = userRepo.findAllByrole(role);
			List<ParticipatedDetails> participantDataList = users.stream().map(i -> getDetailedList(i))
					.collect(Collectors.toList());

			if (!participantDataList.isEmpty()) {
				for (ParticipatedDetails participant : participantDataList) {
					emailService.sendEmailTemplate(participant);
				}
			}
		} catch (Exception e) {
			log.error("Exception occured in sendEmailToParticipant():: {}", e.getMessage());
		}

	}

	public ParticipatedDetails getDetailedList(User user) {
		ParticipatedDetails pData = new ParticipatedDetails();
		int empId = Integer.parseInt(user.getEmployeeId());
		VolunteerDetails volunteerDetail = volunteerRepo.findByemployeeId(empId);
		EventDetail participantDetail = eventDetailRepo.findByemployeeId(empId);
		if (volunteerDetail != null) {
			String role = volunteerDetail.getVolunteerStatus();
			List<FeedbackQuestion> feedbackList = feedbackRepo.findAllByfbType(role);
			pData.setEmailId(user.getEmail());
			pData.setEmployeeId(empId);
			pData.setEventDate(volunteerDetail.getEventDate());
			pData.setEventId(volunteerDetail.getEventId());
			pData.setEventName(volunteerDetail.getEventName());
			pData.setFeedback(feedbackList);
		}
		if (participantDetail != null) {
			String role = "participant";
			List<FeedbackQuestion> feedbackList = feedbackRepo.findAllByfbType(role);
			pData.setEmployeeId(empId);
			pData.setEmailId(user.getEmail());
			pData.setEventDate(participantDetail.getEventDate());
			pData.setEventId(participantDetail.getEventId());
			pData.setEventName(participantDetail.getEventName());
			pData.setFeedback(feedbackList);
		}
		return pData;
	}
}

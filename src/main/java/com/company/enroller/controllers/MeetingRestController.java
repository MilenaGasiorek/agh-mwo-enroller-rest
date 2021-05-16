package com.company.enroller.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	// GET http://localhost:8080/meetings
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	// GET http://localhost:8080/meetings/2

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)

	public ResponseEntity<?> getMeeting(@PathVariable("id") long Id) {
		Meeting meeting = meetingService.findById(Id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// POST localhost:8080/meetings/3

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity<String>(
					"Unable to create. Participant with login '" + meeting.getId() + "' already exists",
					HttpStatus.CONFLICT);

		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);

	}

	
	// PUT http://localhost:8080/meetings/2
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long Id, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(Id);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
       
       foundMeeting.setTitle(meeting.getTitle());
       meetingService.update(foundMeeting);
       
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    
 // DELETE localhost:8080/meetings/2

 	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
 	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long Id) {
 		Meeting meeting = meetingService.findById(Id);
 		if (meeting == null) {
 			return new ResponseEntity(HttpStatus.NOT_FOUND);
 		}
 		meetingService.delete(meeting);
 		return new ResponseEntity<Meeting>(meeting, HttpStatus.NO_CONTENT);
 	}
 	
	// POST http://localhost:8080/meetings/1/participants
 	
	@RequestMapping(value = "{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@PathVariable("id") long id, @RequestBody Participant participant) {
		
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<String>(
					"Unable to create. Meeting with id '" + id + "' not exists",
					HttpStatus.CONFLICT);

		}
		
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity<String>(
					"Unable to create participant with login " + participant.getLogin() + " exists",
					HttpStatus.CONFLICT);
		}
		meetingService.addMeetingParticipant(meeting, participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);

	}

	// GET http://localhost:8080/meetings/1/participants
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
		Collection<Participant> participants = meetingService.getMeetingParticipants(id);
		if (participants.isEmpty()) {
			return new ResponseEntity<String>(
					"No participants found for meeting id " + id,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
	
	// DELETE http://localhost:8080/meetings/1/participants/user2
	
	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<String>(
					"Unable to find. Meeting with id '" + id + "' not exists",
					HttpStatus.NOT_FOUND);
		}
		
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<String>(
					"Unable to find participant with login " + login,
					HttpStatus.NOT_FOUND);
		}
		
		meetingService.deleteParticipant(id, login);
		return new ResponseEntity<Participant>(participant, HttpStatus.NO_CONTENT);
	}
}

package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")

public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	// GET localhost:8080/participants
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// GET localhost:8080/participants/user3
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)

	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	// POST localhost:8080/participants/user3

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity<String>(
					"Unable to create. Participant with login '" + participant.getLogin() + "' already exists",
					HttpStatus.CONFLICT);

		}
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);

	}

	// DELETE localhost:8080/participants/user2

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		//return new ResponseEntity<Participant>(participant, HttpStatus.OK);
		return new ResponseEntity<Participant>(participant, HttpStatus.NO_CONTENT);
	}
	
	
	// PUT http://localhost:8080/participants/user2
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
       
        // wariant 1
       foundParticipant.setPassword(participant.getPassword());
		participantService.update(foundParticipant);
       
        // wariant 2
       // participant.setLogin(login);
        //participantService.update(participant);
       
        return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
    }
}

package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(long Id) {
		Meeting meeting = (Meeting) connector.getSession().get(Meeting.class, Id);
		return meeting;
	}

	public void add(Meeting meeting) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();

	}

	public void update(Meeting foundMeeting) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().merge(foundMeeting);
		transaction.commit();

	}

	public void delete(Meeting meeting) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();

	}

	@SuppressWarnings("unchecked")
	public Collection<Participant> getMeetingParticipants(long id) {
		String hql = "FROM Meeting where id = :id";
		List<Meeting> meetings = connector
			.getSession()
			.createQuery(hql)
			.setParameter("id", id)
			.list();
		if (meetings != null && !meetings.isEmpty()) {
			Meeting meeting = meetings.get(0);
			return meeting.getParticipants();
		}
		return Collections.emptyList();
	}

	public void addMeetingParticipant(Meeting meeting, Participant participant) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		meeting.addParticipant(participant);
		//connector.getSession().save(participant);
		connector.getSession().merge(meeting);
		transaction.commit();	
	}

	@SuppressWarnings("unchecked")
	public void deleteParticipant(long id, String login) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		String hql = "FROM Meeting where id = :id";
		List<Meeting> meetings = connector
			.getSession()
			.createQuery(hql)
			.setParameter("id", id)
			.list();
		if (meetings != null && !meetings.isEmpty()) {
			Meeting meeting = meetings.get(0);
			meeting.removeParticipant(login);
		}
		
	}

}

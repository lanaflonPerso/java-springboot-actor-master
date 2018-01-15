package co.simplon.springboot.actor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.simplon.springboot.actor.dao.ActorDAO;
import co.simplon.springboot.actor.model.Actor;

@Service
public class ActorService {
	
	@Autowired
	private ActorDAO dao;
	
	// Retrieve all rows from table and populate list with objects
	public List<Actor> getAllActors() throws Exception {
		return dao.listActors();
	}
	
	// Retrieves one row from table based on given id
	public Actor getActor(Long id) throws Exception {
		return dao.getActor(id);
	}
	
	// Inserts row into table 
	public Actor addActor(Actor actor) throws Exception {
		return dao.insertActor(actor);
	}
	
	// Updates row in table
	public Actor updateActor(Long id, Actor actor) throws Exception {
		return dao.updateActor(actor);
	}
	
	// Removes row from table
	public void deleteActor(Long id) throws Exception {
		dao.deleteActor(id);
	}
}

package co.simplon.springboot.actor.dao;

import java.util.List;

import co.simplon.springboot.actor.model.Actor;

/**
 * DAO interface for the actor model.
 */
public interface ActorDAO {

	public List<Actor> listActors() throws Exception;

	public Actor getActor(Long id) throws Exception;

	public Actor insertActor(Actor actor) throws Exception;

	public Actor updateActor(Actor actor) throws Exception;
	
	public void deleteActor(Long id) throws Exception;

}

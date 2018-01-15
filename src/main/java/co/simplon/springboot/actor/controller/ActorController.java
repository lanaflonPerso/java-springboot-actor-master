package co.simplon.springboot.actor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.springboot.actor.model.Actor;
import co.simplon.springboot.actor.service.ActorService;

/**
 * The ActorController defines all the rest api for handling actors.
 */
@RestController
@RequestMapping("/api")
public class ActorController {

	@Autowired
	private ActorService actorService;
	
	/**
	 * Get all the actors.
	 * @return a list with all the actors
	 * @throws Exception 
	 */
	@RequestMapping(value = "/actors", method = RequestMethod.GET)
	public ResponseEntity<?> getAllActors(){
		List<Actor> listActor = null;
		try {
			listActor = actorService.getAllActors();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(listActor);
	}
	
	/**
	 * Get a specific actor based on ID
	 * @param id : the id of actor.
	 * @return the actor
	 * @throws Exception 
	 */
	@RequestMapping(value = "/actor/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getActor(@PathVariable Long id){
		Actor actor = null;
				
		try {
			actor =actorService.getActor(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
		if(actor == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		return ResponseEntity.status(HttpStatus.OK).body(actor);
	}
	
	/**
	 * Create a new actor.
	 * @param actor : the actor information.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/actor", method = RequestMethod.POST)
	public ResponseEntity<?> addActor(@RequestBody Actor actor){
		Actor resultActor = null;
		String firstName = actor.getFirstName();
		if((firstName == null) || (firstName.isEmpty()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The firstname is not set !");
		
		String lastName = actor.getLastName();
		if((lastName == null) || (lastName.isEmpty()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The lastname is not set !");
		
		try {
			resultActor = actorService.addActor(actor);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resultActor);
	}
	
	/**
	 * Update an existing actor.
	 * @param actor : the actor information.
	 * @param id : the id of actor.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/actor/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateActor(@RequestBody Actor actor,@PathVariable Long id) throws Exception {
		Actor result = null;
		String firstName = actor.getFirstName();
		if((firstName == null) || (firstName.isEmpty()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The firstname is not set !");
		
		String lastName = actor.getLastName();
		if((lastName == null) || (lastName.isEmpty()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The lastname is not set !");
		
		try {
			result = actorService.updateActor(id, actor);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	/**
	 * Delete an existing actor.
	 * @param id : the id of actor.
	 * @throws Exception
	 */
	@RequestMapping(value = "/actor/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteActor(@PathVariable Long id){
		try {
		actorService.deleteActor(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}

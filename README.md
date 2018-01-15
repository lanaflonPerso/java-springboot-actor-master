# java-springboot-actor

Ce projet est une application SpringBoot basique permettant de gérer une base de données d'acteurs avec les opérations CRUD standards. 
Il utilise la base de données de test Sakila installée avec Mysql.

**Côté front** : un client html javascript natif utilisant les bibliothèques jquery & bootstrap.<br>
**Côté back** : une application Java SpringBoot qui implémente les principes du MVC avec une couche DAO qui communique avec Mysql.

L'objectif de ce tutoriel est de fournir un projet permettant de travailler les aspects suivants : 
 * Analyser un client REST construit avec SpringBoot
 * Comprendre le fonctionnement d'une API REST
 * Voir comment un client web utilise une API REST

# Prérequis

Pour faire fonctionner ce projet, l'installation des composants suivants est conseillée :
 * Eclipse
 * Mysql
 * Eclipse
 * STS - Spring Tool Suite (plugin Eclipse)

Il est aussi recommandé d'avoir fait l'étude du projet java-springboot-decouverte (accessible [ici](https://github.com/simplonco/java-springboot-decouverte))

# Eléments de configuration à étudier

### pom.xml
En plus des éléments classiques vus dans le projet java-springboot-decouverte, nous avons ajouté des dépendances vers :
 * le système de gestion de base de données JDBC (spring-boot-starter-jdbc)
 * le connecteur mysql associé à JDBC (mysql-connector-java)
 * la bibliothèque javascript JQuery
 * le framework UI bootstrap

```xml
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		
		<dependency>
			<groupId>org.webjars</groupId>
			<artifactId>jquery</artifactId>
			<version>2.2.4</version>
		</dependency>

		<dependency>
			<groupId>org.webjars</groupId>
			<artifactId>bootstrap</artifactId>
			<version>3.3.7</version>
		</dependency>
	</dependencies>
```

**Remarque :** Il est possible de forcer l'utilisation d'une version d'une des dépendances en ajoutant une balise **<version>** contenant cette l'identifiant de la version à utiliser. Dans le cas où ce n'est pas spécifié, c'est SpringBoot qui détermine quel est la meilleure version à utiliser.

### application.properties
Vous trouverez dans ce fichier les informations de configuration pour la base de données et pour le sytème de log. 

```properties
# connection base 
spring.datasource.url=jdbc:mysql://localhost/sakila?useSSL=false
spring.datasource.username=admin
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
# log
logging.level.root=INFO
logging.file="c:\logSpring.log"
```

**Remarque :** Si l'application ne fonctionne pas, c'est peut-être dû à vos paramètres mysql ?! C'est ici qu'il vous faudra travailler pour corriger le problème.

# Code source

Notre code source est organisé selon les principes du MVC (Model View Controller) :
 * co.simplon.springboot.actor.model : package contenant les éléments du modèle
 * co.simplon.springboot.actor.controller : package contenant les contrôleurs de l'application
 * co.simplon.springboot.actor.dao : package contenant les classes dao (gestion de la base de données)
 * co.simplon.springboot.actor.serice : package contenant les classes métiers (business)
 
Les éléments relatifs à la Vue sont présents dans le répertoire src/main/resources/static (partie front).
 
Cette structure n'est pas obligatoire mais c'est une bonne pratique de s'en inspirer. Elle permet d'organiser la structure du projet et de regrouper les classes "intelligement".

## Le modèle

#### Actor.java
Il s'agit de la classe de notre modèle de données. Sa structure correspond à la structure de la table associée dans la base de données. On peut la considérer comme un Bean java standard. Il est donc conseillé qu'elle contienne : 
 * un constructeur par défaut
 * des Getters et Setters pour tous les attributs de la classe

## La couche DAO
La couche DAO est la couche qui gère la persistance des données. Cette couche apporte les méthodes CRUD classiques pour les classes du modèle associées.
  
#### ActorDAO.java
Il s'agit d'une interface java classique qui contient les méthodes pour créer, modifier, supprimer et retrouver des données de type **Actor** dans la base de données.  

#### JdbcActorDAO.java
JdbcActorDAO est la classe d'implémentation associée à l'interface **ActorDAO**. Elle porte le code capable de produire et exécuter les requêtes SQL nécessaires à la persistance des données de type **Actor**. Elle est surmontée de l'annotation **@Repository** permettant au système de résolution des dépendances d'identifier les classes "DAO".

```java
@Repository
public class JdbcActorDAO implements ActorDAO {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private DataSource datasource;
```

Pour construire l'attribut datasource, qui gère la connexion avec la base de données, on utilisera la classe **JdbcTemplate** (fournie par Spring). Ainsi on obtiendra une connexion automatiquement configurée avec les informations du fichier **application.properties**.

```java
	@Autowired
	public JdbcActorDAO(JdbcTemplate jdbcTemplate) {
		this.datasource = jdbcTemplate.getDataSource();
	}
```

L'annotation **@Autowired** délègue à Spring la gestion du cycle de vie de cet attribut passé au constructeur qu'elle précède. Avec cette annotation, on demande à Spring de trouver et instancier la classe fournie en argument de notre constructeur.

## Le couche service
C'est la couche métier de l'application.  
C'est ici qu'est décrit le comportement des classes (diagramme des séquences), les transactions, les relations entre les classes.  
Le service sert de transition, est appelé par le contrôleur et agit sur le modèle.
Bien qu'il fasse partie du modèle, il est placé dans le package service.

#### ActorService
Dans un exemple aussi simple que celui-ci, la classe ActorService n'a que peu d'intérêt. On y voit une classe "Passe Plat" qui ne fait que relayer les méthodes de la classe DAO. Pour déclarer son existence à Spring, on la précède de l'annotation @Service

```java
@Service
public class ActorService {
	
	@Autowired
	private ActorDAO dao;
```

**Remarque :** on voit ici une autre utilisation de l'annotation **@Autowired** faite sur un attribut de la classe. L'idée est la même : déléguer la gestion du cycle de vie de dao à Spring. En scannant notre code, Spring cherchera la classe la plus adaptée à être associée à cet attribut et appellera son constructeur au bon moment (durant l'exécution de notre programme).

**Remarque additionnel :** L'attribut dao est une interface. Pour résoudre "l'injection de dépendance" décrite ci-dessus, Spring créera une instance de la JdbcActorDAO qui implémente cette interface qu'il considèrera comme la meilleure classe pour faire le travail (C'est la seul implémentation du projet).

## Le contrôleur

#### ActorController.java
Il s'agit du contrôleur de notre application pour le modèle de donnée Actor. C'est cette classe qui va gérer l'API REST de notre application. Les contrôleurs au sens "Spring" sont repérés par une annotation spéciale

```java
@RestController
@RequestMapping("/api")
public class ActorController {
```

L'annotation **@RestController** est une version spécialisée de l'annotation **@Controller**. Elle gère aussi l'ajout de l'annotation **@ResponseBody** aux méthodes portant les services du contrôleur.

Le controleur possède un attribut faisant une référence à la classe de service ActorService dont la résolution est faite grâce à l'annotation **@Autowired**:

```java
	@Autowired
	private ActorService actorService;
```

Sont ensuite définies les méthodes de la classe du contrôleur.

```java
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
```

Toutes les méthodes de notre API sont précédées de l'annotation **@RequestMapping** qui définira le chemin (fin de l'URL) et la méthode de la requête.
Pour plus d'informations sur les différentes manières d'écrire une annotation **@RequestMapping** , rendez-vous sur http://www.baeldung.com/spring-requestmapping.


**Remarque :** Il possible de renvoyer directement des objets java en retour de fonction. Ces objets sont alors automatiquement sérialisés et renvoyés comme réponse à la requête associée. 
Nous avons choisi d'utiliser la classe **ResponseEntity** fournie par Spring qui permet un plus grand contrôle de la réponse HTTP (gestion des codes http, contenu de requête...).

## La vue

Comme cela a déjà été évoqué, les éléments relatifs à la vue sont présents dans le répertoire main/src/resources/static. Il s'agit en fait d'un client html / javascript classique qui sera inclu au projet et déployé sur le serveur. Ce client utilisera l'API REST pour accéder aux données des acteurs via des requêtes Ajax.

Pour étudier ce fonctionnement, consulter les fichiers :
 * **index.html** : fichier html point d'entrée du client pour le client web.
 * **actor.js** : fichier javascript contenant les fonctions du client web.
 
 **Remarque :** L'API REST que propose notre serveur pourra facilement être utilisés par d'autre clients front. Il serait par exemple possible de l'utiliser via des applications mobiles (android, ios...).

## Observations

Il existe des classes qui ont des fonctions définies
 * contrôleur
 * service
 * entité
 
Il existe une seule vue
 * elle est en HTML dans un dossier précis
 * elle est envoyée une seule fois au client
 * elle envoie des requêtes Ajax au serveur
 * au retour de la requête, elle met en forme les données

Tout est relié grâce à la magie de Spring et des annotations.

# Tester l'API REST
Dans le cadre du développement d'une API REST, il est nécessaire de pouvoir "émuler" les requêtes qu'appelleront les futurs clients front d'une application. Les 2 outils suivant vous aideront dans ce travail :
 * Curl
 * Postman


## Curl
Curl est un client en ligne de commande permettant de "créer" des requêtes HTTP. On passe lui passe directement les informations de la requêtes comme arguments lors de l'appel. Très complet, c'est l'"ami" des administrateurs systèmes car on peut facilement l'appeler dans des scripts. Pour en savoir plus sur Curl, rendez-vous sur https://ec.haxx.se/.  

Quelques exemples d'utilisation :
 * curl -i -X POST -H "Content-Type: application/json" -d "{\"key\":\"val\"}" http://localhost:8080/appname/path
 * curl -X POST  -H "Accept: text/plain" -H "Content-Type:application/json" --data @newactor.json http://localhost:8080/api/actor

## Postman
Postman est un client équivalent à CURL qui fournit une interface graphique plus sympathique que Curl.
Il permet de créer des collections, sauvegarde les requêtes jouées et peut même aller jusqu'à la création de scénarii de test.  

Les liens ci-dessous vous aideront à voir comment il fonctionne :
* [article](https://amethyste16.wordpress.com/2016/02/24/tutoriel-postman/)
* [postman](https://www.getpostman.com/)

# Quelques conseils pour la suite

Spring est un framework difficile à prendre en main car son champ d'application est très vaste. Il fournit une quantité importante de classes utilisables pour différents contextes / besoins et une quantité phénoménale d'annotations auxquelles viennent s'ajouter les annotations des librairies que Spring gère. De plus il est souvent possible d'écrire ces annotations de plusieurs manières. 

Vous l'aurez compris, une grande partie du travail à venir sera donc de se familiariser avec tous ces éléments (les annotations particulièrement).

## Annotations à étudier

Dans la version actuelle de SpringBoot, les annotations ont une importance particulière. Il en existe un grand nombre cependant si vous maîtriser la listes des annotations ci-dessous, vous aurez une base solide pour être autonome. Nous vous conseillons donc d'étudier cette liste que nous avons organisées par "thématique".

### Annotations de configuration

@SpringBootApplication<br>
@Configuration<br>
@ComponentScan<br>
@EnableWebMvc<br>
@Value<br>

### Annotations liées à l'injection de dépendances

@Bean<br>
@Component<br>
@Service<br>
@Repository<br>
@Autowired<br>
@Qualified<br>

### Annotations pour le web

@Controller<br>
@RestController<br>
@RequestMapping<br>
@RequestParam<br>
@RequestBody<br>
@ResponseBody<br>
@PathVariable<br>

## Un peu de lecture
10 raisons pour se mettre à SpringBoot : 
[1ère partie](http://blog.ellixo.com/2015/06/08/10-raisons-de-se-mettre-a-Spring-Boot-1ere-partie.html), 
[2ème partie](http://blog.ellixo.com/2015/06/26/10-raisons-de-se-mettre-a-Spring-Boot-2eme-partie.html)

### Liens spring.io sélectionnés
* [Spring Boot référence guide](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)
* [Spring Boot référence](http://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/reference/htmlsingle/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Consuming a RESTful Web Service](https://spring.io/guides/gs/consuming-rest/)
* [Consuming a RESTful Web Service with jQuery](https://spring.io/guides/gs/consuming-rest-jquery/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Producing a SOAP web service](https://spring.io/guides/gs/producing-web-service/)
* [Consuming a SOAP web service](https://spring.io/guides/gs/consuming-web-service/)
* [Validating Form Input](https://spring.io/guides/gs/validating-form-input/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Integrating Data](https://spring.io/guides/gs/integration/)
* [Testing the Web Layer](https://spring.io/guides/gs/testing-web/)

### webographie
* [spring reference](http://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/html/)
* [guides spring](https://spring.io/guides)
* [doc reference spring](https://spring.io/docs/reference)
* [exemples sur github](https://github.com/netgloo/spring-boot-samples)
* [spring batch](http://blog.octo.com/spring-batch-par-quel-bout-le-prendre/)
* [mvc](http://orm.bdpedia.fr/mvc.html)
* [jackson](https://www.tutorialspoint.com/jackson/index.htm) : Une librairie pour manipuler les objets JSON.
* [une API Spring REST](http://idak.developpez.com/tutoriels/spring/creation-restfull-serviceweb/) en Spring sans Spring-boot.
* [tuto Spring developpez.com](http://rpouiller.developpez.com/tutoriels/spring/application-web-spring-hibernate/)

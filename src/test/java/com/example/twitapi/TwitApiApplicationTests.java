package com.example.twitapi;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.repository.TweetRepository;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.repository.UserRepository;
import com.example.twitapi.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
class TwitApiApplicationTests {

    @Value("${server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TweetRepository tweetRepository;

    @BeforeAll
    static void dropAllCollections() {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://shalihin:130194@cluster0.f3owvmq.mongodb.net")) {
            MongoDatabase database = mongoClient.getDatabase("twitter-test");
            MongoCollection<Document> usersCollection = database.getCollection("users");
            MongoCollection<Document> tweetsCollection = database.getCollection("tweets");
            MongoCollection<Document> repliesCollection = database.getCollection("replies");
            usersCollection.drop();
            tweetsCollection.drop();
            repliesCollection.drop();
            System.out.println("Users, tweets and replies collection dropped successfully");
        }
    }

    @Test
    @Order(1)
    void shouldCreateUsersForPost() {

        String uri = "http://localhost:%s/api/users".formatted(port);

        User user1 = new User(
                "cindy",
                "cindy@mail.com"
        );
        User user2 = new User(
                "peter",
                "peter@mail.com"
        );
        User user3 = new User(
                "jack",
                "jack@mail.com"
        );

        ResponseEntity<User> exchangeUser1 = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(user1), User.class);
        assertThat(exchangeUser1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(exchangeUser1.getBody()).getUserId()).isNotNull();
        assertThat(exchangeUser1.getBody().getUserName()).isNotNull();
        assertThat(exchangeUser1.getBody().getUserEmail()).isNotNull();
        assertThat(exchangeUser1.getBody().getUserFollower()).isEmpty();
        assertThat(exchangeUser1.getBody().getUserFollowing()).isEmpty();

        ResponseEntity<User> exchangeUser2 = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(user2), User.class);
        assertThat(exchangeUser2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(exchangeUser2.getBody()).getUserId()).isNotNull();
        assertThat(exchangeUser2.getBody().getUserName()).isNotNull();
        assertThat(exchangeUser2.getBody().getUserEmail()).isNotNull();
        assertThat(exchangeUser2.getBody().getUserFollower()).isEmpty();
        assertThat(exchangeUser2.getBody().getUserFollowing()).isEmpty();

        ResponseEntity<User> exchangeUser3 = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(user3), User.class);
        assertThat(exchangeUser3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(exchangeUser3.getBody()).getUserId()).isNotNull();
        assertThat(exchangeUser3.getBody().getUserName()).isNotNull();
        assertThat(exchangeUser3.getBody().getUserEmail()).isNotNull();
        assertThat(exchangeUser3.getBody().getUserFollower()).isEmpty();
        assertThat(exchangeUser3.getBody().getUserFollowing()).isEmpty();
    }

    @Test
    @Order(2)
    void peterShouldFollowCindy() {

        String user = "peter";
        String userToFollow = "cindy";

        String url = "http://localhost:%s/api/users/%s/follow-user".formatted(port, user);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", userToFollow)
                .build()
                .toUri();

        ResponseEntity<User> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, HttpEntity.EMPTY, User.class);
        User peter = exchange.getBody();

        assert peter != null;
        assertThat(peter.getUserFollowing()).hasSize(1);
    }

    @Test
    @Order(2)
    void jackShouldFollowCindy() {
        String user = "jack";
        String userToFollow = "cindy";
        String url = "http://localhost:%s/api/users/%s/follow-user".formatted(port, user);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", userToFollow)
                .build()
                .toUri();
        ResponseEntity<User> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, HttpEntity.EMPTY, User.class);
        User jack = exchange.getBody();
        assertThat(jack.getUserFollowing()).hasSize(1);
    }

    @Test
    @Order(3)
    void cindyShouldHavePeterAndJackAsFollowers() {
        User cindy = userRepository.findUserByUserName("cindy");
        assertTrue(cindy.getUserFollower().contains("peter"));
        assertTrue(cindy.getUserFollower().contains("jack"));
    }

    @Test
    @Order(4)
    void createUserWithExistingUsernameResultsIn409() {
        String uri = "http://localhost:%s/api/users".formatted(port);
        User jack = new User("jack", "jack@mail.com");
        ResponseEntity<User> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(jack), User.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @Order(5)
    void shouldNotAllowUserToBeCreatedTwice() {
        User jack = new User("jack", "jack@mail.com");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(jack));
        assertEquals(exception.getMessage(), "User already exists");
    }

    @Test
    @Order(6)
    void userFollowersShouldBeUnique() {
        String user = "jack";
        String userToFollow = "cindy";
        String url = "http://localhost:%s/api/users/%s/follow-user".formatted(port, user);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", userToFollow)
                .build()
                .toUri();
        ResponseEntity<User> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, HttpEntity.EMPTY, User.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @Order(7)
    void userShouldNotBeAbleToUnfollowIfNotFollowingAtFirst() {
        String user = "cindy";
        String userToUnfollow = "peter";
        String url = "http://localhost:%s/api/users/%s/unfollow-user".formatted(port, user);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", userToUnfollow)
                .build()
                .toUri();
        ResponseEntity<User> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, HttpEntity.EMPTY, User.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(8)
    void shouldNotAllowUserToFollowSameUserTwice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.followUser("jack", "cindy"));
        assertEquals(exception.getMessage(), "User is already a follower");
    }

    @Test
    @Order(8)
    void shouldNotAllowUserToUnfollowIfNotFollowingAtFirst() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.unfollowUser("cindy", "peter"));
        assertEquals(exception.getMessage(), "User is not a follower");
    }

    @Test
    @Order(9)
    void shouldReturn2ForNumberOfCindyFollowers() throws JsonProcessingException {
        String user = "cindy";
        String uri = "http://localhost:%s/api/users/%s/followers".formatted(port, user);

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, application/*+json");

        ResponseEntity<String> exchange = new TestRestTemplate()
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JsonNode userFollowers = mapper.readTree(exchange.getBody());
        assertThat(userFollowers.size()).isEqualTo(2);
    }

    @Test
    @Order(9)
    void cindyShouldBeAbleToComposeATweet() {
        String user = "cindy";
        String url = "http://localhost:%s/api/tweets/compose".formatted(port);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", user)
                .build()
                .toUri();
        Tweet tweetContent = new Tweet("I am Cindy. This is my 1st tweet");
        ResponseEntity<Tweet> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(tweetContent), Tweet.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(exchange.getBody().getTweetId()).isNotNull();
        assertThat(exchange.getBody().getTweetContent()).isEqualTo("I am Cindy. This is my 1st tweet");
        assertThat(exchange.getBody().getTweetReplies()).isEmpty();
        assertThat(exchange.getBody().getUser().getUserName()).isEqualTo("cindy");
    }

    @Test
    @Order(9)
    void cindyShouldBeAbleToComposeAnotherTweet() {
        String user = "cindy";
        String url = "http://localhost:%s/api/tweets/compose".formatted(port);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", user)
                .build()
                .toUri();
        Tweet tweetContent = new Tweet("I am Cindy. This is my other tweet");
        ResponseEntity<Tweet> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(tweetContent), Tweet.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(exchange.getBody().getTweetId()).isNotNull();
        assertThat(exchange.getBody().getTweetContent()).isEqualTo("I am Cindy. This is my other tweet");
        assertThat(exchange.getBody().getTweetReplies()).isEmpty();
        assertThat(exchange.getBody().getUser().getUserName()).isEqualTo("cindy");
    }

    @Test
    @Order(9)
    void jackShouldBeAbleToComposeATweet() {
        String user = "jack";
        String url = "http://localhost:%s/api/tweets/compose".formatted(port);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", user)
                .build()
                .toUri();
        Tweet tweetContent = new Tweet("I am Jack. This is my 1st tweet");
        ResponseEntity<Tweet> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(tweetContent), Tweet.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(exchange.getBody().getTweetId()).isNotNull();
        assertThat(exchange.getBody().getTweetContent()).isEqualTo("I am Jack. This is my 1st tweet");
        assertThat(exchange.getBody().getTweetReplies()).isEmpty();
        assertThat(exchange.getBody().getUser().getUserName()).isEqualTo("jack");
    }

    @Test
    @Order(10)
    void getCindyTweetsShouldReturn2() throws JsonProcessingException {
        String user = "cindy";
        String uri = "http://localhost:%s/api/tweets/%s".formatted(port, user);

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, application/*+json");

        ResponseEntity<String> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JsonNode userTweets = mapper.readTree(exchange.getBody());
        assertThat(userTweets.size()).isEqualTo(2);
    }

    @Test
    @Order(11)
    void jackShouldBeAbleToReplyToCindyFirstTweet() {
        String userName = "jack";
        String userNameReplyTo = "cindy";
        String tweetContent = "I am Cindy. This is my 1st tweet";

        User userCindy = userRepository.findUserByUserName("cindy");
        Tweet userSpecificTweet = tweetRepository.getTweetByUserAndTweetContent(userCindy, tweetContent);
        String tweetId = userSpecificTweet.getTweetId();

        String url = "http://localhost:%s/api/replies/tweet".formatted(port);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", userName)
                .queryParam("to", userNameReplyTo)
                .queryParam("tweetId", tweetId)
                .build()
                .toUri();

        Reply replyContent = new Reply("I'm Jack. This is my reply to Cindy's first tweet!");
        ResponseEntity<Reply> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(replyContent), Reply.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Order(11)
    void cindyShouldNotBeAbleToReplyToJackFirstTweetSinceCindyIsNotFollowingJack() {
        String userName = "cindy";
        String userNameReplyTo = "jack";
        String tweetContent = "I am Jack. This is my 1st tweet";

        User userJack = userRepository.findUserByUserName(userNameReplyTo);
        Tweet userSpecificTweet = tweetRepository.getTweetByUserAndTweetContent(userJack, tweetContent);
        String tweetId = userSpecificTweet.getTweetId();

        String url = "http://localhost:%s/api/replies/tweet".formatted(port);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", userName)
                .queryParam("to", userNameReplyTo)
                .queryParam("tweetId", tweetId)
                .build()
                .toUri();

        Reply replyContent = new Reply("I'm Cindy. This is my reply to Jack's first tweet!");
        ResponseEntity<Reply> exchange = restTemplate
                .withBasicAuth("admin", "password")
                .exchange(uri, HttpMethod.POST, new HttpEntity<>(replyContent), Reply.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(12)
    void userRoleAuthShouldNotBeAbleToDeleteExistingUser() {
        String userName = "peter";
        String uri = "http://localhost:%s/api/users/%s".formatted(port, userName);
        ResponseEntity<Reply> exchange = restTemplate
                .withBasicAuth("user", "password")
                .exchange(uri, HttpMethod.DELETE, HttpEntity.EMPTY, Reply.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(13)
    void pinOneTweetShouldReturn200() {
        String userName = "cindy";
        String tweetContent = "I am Cindy. This is my 1st tweet";
        boolean pinned = true;

        User userCindy = userRepository.findUserByUserName(userName);
        Tweet userSpecificTweet = tweetRepository.getTweetByUserAndTweetContent(userCindy, tweetContent);
        String tweetId = userSpecificTweet.getTweetId();

        String url = "http://localhost:%s/api/tweets/%s/status/%s".formatted(port, userName, tweetId);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("pinned", pinned)
                .build()
                .toUri();

        ResponseEntity<Reply> exchange = restTemplate
                .withBasicAuth("user", "password")
                .exchange(uri, HttpMethod.PUT, HttpEntity.EMPTY, Reply.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(14)
    void attemptToPinMoreThanOneTweetShouldReturn409() {
        String userName = "cindy";
        String tweetContent = "I am Cindy. This is my other tweet";
        boolean pinned = true;

        User userCindy = userRepository.findUserByUserName(userName);
        Tweet userSpecificTweet = tweetRepository.getTweetByUserAndTweetContent(userCindy, tweetContent);
        String tweetId = userSpecificTweet.getTweetId();

        String url = "http://localhost:%s/api/tweets/%s/status/%s".formatted(port, userName, tweetId);
        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("pinned", pinned)
                .build()
                .toUri();

        ResponseEntity<Reply> exchange = restTemplate
                .withBasicAuth("user", "password")
                .exchange(uri, HttpMethod.PUT, HttpEntity.EMPTY, Reply.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}

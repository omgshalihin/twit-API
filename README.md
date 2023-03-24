# TWIT-API
### Background
An API/service that is built with Java to mimic the behavior of Twitter. This API is built ontop of [Spring](https://spring.io/) framework and utilises [MongoDB Atlas](https://www.mongodb.com/atlas/database) as a cloud database provider.

### Hosting/Deployment
The following domains are hosted on [Railway.app](https://railway.app/)
- https://twit-api-production.up.railway.app/api/users
- https://twit-api-production.up.railway.app/api/tweets
- https://twit-api-production.up.railway.app/api/replies

### To Get Started (two methods)
#### Authorization Feature in Postman Client
inside postman client under authoriszation tab, fill in the following:
- Type: `Basic Auth`
- Username: `admin` or `user`
- Password: `password`

OR add the following as a header
- header key: `Authorization`
- header value for `ADMIN role`: `Basic YWRtaW46cGFzc3dvcmQ=`
- header value for `USER role`: `Basic dXNlciBwYXNzd29yZA==`

important note:
- `ADMIN Role` is able to communicate with all API methods
- `USER Role` is able to communicate with all API methods ****except**** for:
  - Get all users `GET`
  - Get all tweets `GET`
  - Create new user `POST`
  - Delete user `DELETE`

#### Method 1: use the [Host Domains](https://github.com/omgshalihin/twit-API#hosting) provided and [API Guides](https://github.com/omgshalihin/twit-API#users-api-guide) to communicate with the service. 

For example,
-  use https://twit-api-production.up.railway.app/api/users instead of http://localhost:8080/api/users

#### Method 2: clone and run service locally
- clone this git repository into your local folder
```
git clone 
```
- go into the folder and open with your favorite IDE (intelliJ)
```
cd <folder> && idea pom.xml
```
- inside `main/resources` & `test/resources` folders, create a file called `env.properties` and then update the MongoDB Atlas `connection string`
```
DB_USER=<mongoDB_user>
DB_PWD=<mongoDB_password>
DB_ENDPOINT=<mongoDB_endpoint>
DB_NAME=<mongoDB_name>
```
- run `TwitApiApplication` to host it locally at http://localhost:8080/api/users, http://localhost:8080/api/tweets, http://localhost:8080/api/replies
- use the respective `API Guides` ([Users API Guide](https://github.com/omgshalihin/twit-API#users-api-guide), [Tweets API Guide](https://github.com/omgshalihin/twit-API#tweets-api-guide), [Replies API Guide](https://github.com/omgshalihin/twit-API#replies-api-guide)) to communicate with the REST endpoints on a client (e.g. postman)

## Users API Guide

### create a new user `POST`
  ```
  http://localhost:8080/api/users
  ```
  - JSON body
  ```
  {
      "userName": "jack",
      "userEmail": "jack@mail.com"
  }
  ```
  - sample output
  ```
  {
      "userId": "641c599463a2a40208bb376d",
      "userName": "jack",
      "userEmail": "jack@mail.com",
      "userFollowing": [],
      "userFollower": []
  }
  ```

### follow a user `POST`
  ```
  http://localhost:8080/api/users/<userName>/follow-user?username=<userNameToFollow>
  ```
  - sample URI
  ```
  http://localhost:8080/api/users/jack/follow-user?username=cindy
  ```
  - sample output
  ```
  {
      "userId": "641c599463a2a40208bb376d",
      "userName": "jack",
      "userEmail": "jack@mail.com",
      "userFollowing": [
          "cindy"
      ],
      "userFollower": []
  }
  ```

### unfollow a user `POST`
  ```
  http://localhost:8080/api/users/<userName>/unfollow-user?username=<userNameToUnfollow>
  ```
  - sample URI
  ```
  http://localhost:8080/api/users/jack/unfollow-user?username=cindy
  ```
  - sample output
  ```
  {
      "userId": "641c599463a2a40208bb376d",
      "userName": "jack",
      "userEmail": "jack@mail.com",
      "userFollowing": [],
      "userFollower": []
  }
  ```
### delete a user `DELETE`
  ```
  http://localhost:8080/api/users/<userName>
  ```
### retrieve all users `GET`
  ```
  http://localhost:8080/api/users
  ```
### retrieve a user `GET`
  ```
  http://localhost:8080/api/users/<userName>
  ```
### retrieve a list of user's following `GET`
  ```
  http://localhost:8080/api/users/<userName>/following
  ```
### retrieve a list of user's followers `GET`
  ```
  http://localhost:8080/api/users/<userName>/followers
  ```


## Tweets API Guide

### compose a tweet `POST`
  ```
  http://localhost:8080/api/tweets/compose?username=<userName>
  ```
  - JSON body
  ```
  {
      "tweetContent" : "I'm Cindy. This is my 1st tweet!"
  }
   ```
  - sample output
  ```
  {
      "tweetId": "641c6ed742f8ee5a062848b6",
      "tweetContent": "I'm Cindy. This is my 1st tweet!",
      "tweetReplies": [],
      "user": {
          "userId": "641c598263a2a40208bb376c",
          "userName": "cindy",
          "userEmail": "cindy@mail.com",
          "userFollowing": [],
          "userFollower": []
      }
  }
  ```
  
### delete a tweet `DELETE`
  ```
  http://localhost:8080/api/tweets/<tweetId>
  ```

### retrieve all tweets `GET`
  ```
  http://localhost:8080/api/tweets
  ```

### retrieve a list of user's tweets `GET`
  ```
  http://localhost:8080/api/tweets/<userName>
  ```
  
### retrieve a user's specific tweet `GET`
  ```
  http://localhost:8080/api/tweets/<userName>/status/<tweetId>
  ```

## Replies API Guide

### reply to a tweet `POST`
  ```
  http://localhost:8080/api/replies/tweet?username=<userName>&to=<userNameToReply>&tweetId=<tweetId>
  ```
  - JSON body
  ```
  {
      "replyContent" : "I'm Jack. I am replying to Cindy's 1st tweet!"
  }
  ```
  - sample output
  ```
  {
        "tweetId": "641c6ed742f8ee5a062848b6",
        "tweetContent": "I'm Cindy. This is my 1st tweet!",
        "tweetReplies": [
            {
                "replyId": "641c8744c5af2357dd7a8c63",
                "replyContent": "I'm Jack. I am replying to Cindy's 1st tweet!",
                "user": {
                    "userId": "641c599463a2a40208bb376d",
                    "userName": "jack",
                    "userEmail": "jack@mail.com",
                    "userFollowing": [
                        "cindy"
                    ],
                    "userFollower": []
                },
                "userReplyTo": {
                    "userId": "641c598263a2a40208bb376c",
                    "userName": "cindy",
                    "userEmail": "cindy@mail.com",
                    "userFollowing": [],
                    "userFollower": [
                        "jack"
                    ]
                }
            }
        ],
        "user": {
            "userId": "641c598263a2a40208bb376c",
            "userName": "cindy",
            "userEmail": "cindy@mail.com",
            "userFollowing": [],
            "userFollower": [
                "jack"
            ]
        }
  }
  ```

### delete a reply `DELETE`
  ```
  http://localhost:8080/api/replies/delete?tweetId=<tweetId>&replyId=<replyId>
  ```

### retrieve a list of user's replies `GET`
  ```
  http://localhost:8080/api/replies/<userName>
  ```

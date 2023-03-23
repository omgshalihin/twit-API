## Users API

### Create new user `POST`
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

### Follow a user `POST`
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

### Unfollow a user `POST`
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
  
### Get all users `GET`
  ```
  http://localhost:8080/api/users
  ```
### Get user `GET`
  ```
  http://localhost:8080/api/users/<userName>
  ```
### Get user's following `GET`
  ```
  http://localhost:8080/api/users/<userName>/following
  ```
### Get user's followers `GET`
  ```
  http://localhost:8080/api/users/<userName>/followers
  ```


## Tweets API

### Compose tweet `POST`
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
  
### Delete tweet `DELETE`
  ```
  http://localhost:8080/api/tweets/<tweetId>
  ```

### Get all tweets `GET`
  ```
  http://localhost:8080/api/tweets
  ```

### Get user's tweets `GET`
  ```
  http://localhost:8080/api/tweets/<userName>
  ```
  
### Get user's specific tweet `GET`
  ```
  http://localhost:8080/api/tweets/<userName>/status/<tweetId>
  ```

## Replies API

### Reply tweet `POST`
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

### Delete reply `DELETE`
  ```
  http://localhost:8080/api/replies/delete?tweetId=<tweetId>&replyId=<replyId>
  ```

### Get user replies `GET`
  ```
  http://localhost:8080/api/replies/<userName>
  ```

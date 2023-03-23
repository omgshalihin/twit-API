# twit-API

The focus is on building a small backend API.

# tweets API

- ## Compose tweet `POST`
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
  
- ## Delete tweet `DELETE`
    ```
    http://localhost:8080/api/tweets/<tweetId>
    ```

- ## Get all tweets `GET`
    ```
    http://localhost:8080/api/tweets
    ```
- ## Get user's tweets `GET`
    ```
    http://localhost:8080/api/tweets/<userName>
    ```
- ## Get user's specific tweet `GET`
    ```
    http://localhost:8080/api/tweets/<userName>/status/<tweetId>
    ```

# replies API

- ## Reply tweet `POST`
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

- ## Delete reply `DELETE`
    ```
    http://localhost:8080/api/replies/delete?tweetId=<tweetId>&replyId=<replyId>
    ```

- ## Get user replies `GET`
    ```
    http://localhost:8080/api/replies/<userName>
    ```

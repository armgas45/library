
# Library app

This is a sample library app created with Spring boot.




## API Reference

#### Create new admin
```
POST /api/v1/admin

Accessible only with super-admin role. Creates new admins

body: 
{
    "name": String,
    "phone": String,
    "email": String,
    "password": String,
}
```




#### Get Sales Report
Accessible only with super-admin role. Provides some analytical data

```
  GET /api/v1/admin/sales/report
```

#### Get User By Id
Accessible only with admin/super-admin role. Gives the user by id

```
  GET /api/v1/admin/users/{Id}
```


#### Add a Book
```
POST /api/v1/admin/books

Accessible only for admin role. Creates a book.

body: 
{
    "title": String,
    "author": String,
    "genre": String,
    "description": String,
    "isbn": String,
    "image": String,
    "published": String,
    "publisher": String,
    "price": Double,
    "currency": String,
    "available": Boolean
}
```

#### Get all books
```
GET /api/v1/admin/books

Accessible only for admin role. Returns all books.
```

#### Get Book By Id
```
GET /api/v1/admin/books/{id}

Accessible only for admin role. Gives the book by id.
```

#### Update Book By Id
```
PUT /api/v1/admin/books/{id}

Accessible only for admin role. Updates a book.

body: 
{
    "title": String,
    "author": String,
    "genre": String,
    "description": String,
    "isbn": String,
    "image": String,
    "published": String,
    "publisher": String,
    "price": Double,
    "currency": String,
    "available": Boolean
}
```

#### Delete Book By Id
```
DELETE /api/v1/admin/books/{id}

Accessible only for admin role. Deletes the book.
```






#### Sign in
```
POST /api/v1/auth/token

body:
{
    "username": String,
    "password": String
}

default users: 
 1. With user role { "username": "john@doe.com", "password": "1234" }
 2. With admin role { "username": "admin@gmail.com", "password": "1234" }
 3. With super-admin role { "username": "super-admin@gmail.com", "password": "1234" }

Accessible only for everyone.
```


#### Refresh access token
```
POST /api/v1/auth/token/refresh

body:
{
    "refreshToken": String
}

Accessible only for everyone.
```

#### Privacy Policy
```
GET /api/v1/privacy

Accessible only for everyone.
```

#### Terms & Conditions
```
GET /api/v1/terms

Accessible only for everyone.
```


#### Sign Up
```
POST /api/v1/users/signup

body:
{
    "name": String,
    "phone": String,
    "email": String,
    "address": String,
    "postalZip": String,
    "country": String,
    "password": String,
    "preferences": List<String>,
    "pan": String,
    "expdate": String,
    "cvv": String
}

Accessible only for everyone.
```





#### Suggest a book
```
POST /api/v1/users/{id}/suggest

Request Params: [
    limit: Integer
]

Accessible only for user role.
```



#### Search for a book
```
POST /api/v1/users/search

Request Params: [
    keyword: String(book author / book title),
    page: Integer,
    limit: Integer
]


Accessible only for user role.
```


#### Purchase a book
```
POST /api/v1/users/purchase

body:
{
    "userId": Integer,
    "book-list": [
        {
            "bookId": Long,
            "quantity": Integer
        }
    ],
    "payment-details": {
        "payment-option": String,
        "promocode": String
    }
}

Accessible only for user role.
```


#### Log out
```
POST /api/v1/auth/logout

Headers: [
    "refresh-token": String
    "Authorization": String
]

Accessible only for authenticated users.
```
## Run Locally

Clone the project

```bash
  git clone https://github.com/armgas45/library-app.git
```

Go to the project directory and make sure you run

```bash
  docker compose up
```
## Microservice break-down plan


                                api-gateway
                                    |
                                    |
                     _ _ _ _ _ _ _ _|_ _ _ _ _ _ _ _
                    |                               |
                auth-server                     book-service
                    |                               |
                    |                               |
                    |                               |
                    |                               |
               orders-service                payment-gateway


## 

##### 1. Api gateway will act as an edge server and router for our services. It will be our only publicly accessible service due to the k8s load-balancer service. All the rest will be private services from outside of our cluster.


##### 2. Auth-server will act as an ouath2 authorization server. It will manage our users, and authorization flow.

##### 3. In our Book-service we can have all the logic related to the library.

##### 4. Our payment gateway will act as a payment system, probably connected to some PSP(aka. Stripe or SquareUp).

##### 5. Our orders-service will keep track of book ordering. We will probably need to implement a logic here which will notify our library staff about a new order in realtime(aka. Firebase or standard Web Sockets).
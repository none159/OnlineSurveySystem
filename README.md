# Getting Started
## Change url of you're choosing database :

```
 Connection connection = DriverManager.getConnection("jdbc:mysql://host/database","user","password");
```

## Create Necessary Table Query : 

```
CREATE TABLE `feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `rating` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
```

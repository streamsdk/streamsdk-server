drop table Media;
drop table MediaUpload;
drop table User;

CREATE TABLE User
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   password INT NOT NULL,
   MSISDN   VARCHAR(30),
   email VARCHAR(60),
   name VARCHAR (30),
   currency INT NOT NULL DEFAULT 0,
   free INT NOT NULL,
   PRIMARY KEY (id)
)CHARACTER SET GB2312;
CREATE TABLE MediaUpload
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	fileId CHAR(36) NOT NULL,
	mimeType VARCHAR(30) NOT NULL,
	fileSize INT NOT NULL,
	uploadedSize INT NOT NULL,
	PRIMARY KEY (id)
)CHARACTER SET GB2312;

CREATE TABLE Media
(
   mediaId BIGINT NOT NULL AUTO_INCREMENT,
   mediauploadId BIGINT,
   userId BIGINT,
   sent BIT(1) NOT NULL DEFAULT 0,
   path VARCHAR(200),
   address VARCHAR (255),
   fileSize INT NOT NULL,
   dateCreated DATETIME NOT NULL,
   PRIMARY KEY (mediaId)
)CHARACTER SET GB2312;



alter table Media add constraint Media_MediaUpload_FK foreign key (mediauploadId) references MediaUpload (id);
alter table Media add constraint User_Media_FK foreign key (userId) references User (id);



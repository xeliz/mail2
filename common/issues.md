
A decentralized protocol for sending and receiving messages. It works like emails, but used HTTP and JSON and supports both sending and receiving unlike SMTP and POP3/IMAP4. Authorization is supported. HTTPS use is assumed but not required.


# ISSUES

## use bugtracker instead of this file.

## "receive" should support date/time ranges.

## sign up API.

## web client (going to be React).

## statuses in database should be numbers, not strings.

## refactor Mail2ActionHandler

## Mail2RequestDTO validation: https://habr.com/ru/post/424819/

## handle network errors: instead of setting "ERROR" delivery status, several attempts with specific interval should be done in background.

## Separate delivery statuses from mails: a new delivery status table: id, mail2_id, service_address, status, comment (=Mail2ResponseDTO.message).

## Auth with Spring Security:
- Explicit access token generation instead of headers
- google://spring security authenticate manually
- https://stackoverflow.com/questions/4664893/how-to-manually-set-an-authenticated-user-in-spring-security-springmvc

## Messenger features
Messengers support private messages, groups and channels. These may not be only core mail2 features but also done with bots. A group is just a technical user whom admins send commands to. The commands are like "add user", "appoint as admin", "allow mails from everyone", "send message". 


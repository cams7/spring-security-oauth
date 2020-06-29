/* client_secret column is set as the encrypted value of "secret" - the password for the clients  */
/*password: secret*/
INSERT INTO oauth_client_details(
	client_id, 
	client_secret, 
	scope, 
	authorized_grant_types,
	web_server_redirect_uri, 
	authorities, 
	access_token_validity,
	refresh_token_validity, 
	additional_information, 
	autoapprove
) VALUES (
	'fooClientIdPassword', 
	'$2a$10$F2dXfNuFjqezxIZp0ad5OeegW43cRdSiPgEtcetHspiNrUCi3iI6O', 
	'foo,read,write',
	'password,authorization_code,refresh_token,client_credentials', 
	'http://localhost:8089/', 
	null, 
	180,/*3 min*/ 
	300,/*5 min*/ 
	null, 
	'true'
);
/*password: secret*/
INSERT INTO oauth_client_details(
	client_id, 
	client_secret, 
	scope, 
	authorized_grant_types,
	web_server_redirect_uri, 
	authorities, 
	access_token_validity,
	refresh_token_validity, 
	additional_information, 
	autoapprove
) VALUES (
	'sampleClientId', 
	'$2a$10$F2dXfNuFjqezxIZp0ad5OeegW43cRdSiPgEtcetHspiNrUCi3iI6O', 
	'read,write,foo,bar',
	'implicit', 
	'http://localhost:8086/', 
	null, 
	180,/*3 min*/ 
	300,/*5 min*/ 
	null, 
	'true'
);
/*password: secret*/
INSERT INTO oauth_client_details(
	client_id, client_secret, 
	scope, 
	authorized_grant_types,
	web_server_redirect_uri, 
	authorities, 
	access_token_validity,
	refresh_token_validity, 
	additional_information, 
	autoapprove
) VALUES (
	'barClientIdPassword', 
	'$2a$10$F2dXfNuFjqezxIZp0ad5OeegW43cRdSiPgEtcetHspiNrUCi3iI6O', 
	'bar,read,write',
	'password,authorization_code,refresh_token,client_credentials', 
	'http://www.example.com', 
	null, 
	180,/*3 min*/ 
	300,/*5 min*/ 
	null, 
	'true'
);
/*password: abc123*/
INSERT INTO oauth_client_details(
	client_id, 
	client_secret, 
	scope,
	resource_ids,
	authorized_grant_types,
	web_server_redirect_uri, 
	authorities, 
	access_token_validity,
	refresh_token_validity, 
	additional_information, 
	autoapprove
) VALUES (
	'employee-service', 
	'$2a$10$dGpnHnWnbYekvihv0UzfFOcXWMXEG65pAffqBgQsgx.0eoNFLZFAS', 
	'EMPLOYEE,DEPARTMENT',
	'employee-service,department-service',
	'password,authorization_code,refresh_token,client_credentials', 
	'http://localhost:8090/', 
	'ROLE_READ_EMPLOYEE,ROLE_READ_DEPARTMENT', 
	180,/*3 min*/ 
	300,/*5 min*/ 
	null, 
	'true'
);
/*password: abc123*/
INSERT INTO oauth_client_details(
	client_id, 
	client_secret, 
	scope, 
	resource_ids,
	authorized_grant_types,
	web_server_redirect_uri, 
	authorities, 
	access_token_validity,
	refresh_token_validity, 
	additional_information, 
	autoapprove
) VALUES (
	'department-service', 
	'$2a$10$dGpnHnWnbYekvihv0UzfFOcXWMXEG65pAffqBgQsgx.0eoNFLZFAS', 
	'DEPARTMENT',
	'department-service',
	'implicit', 
	'http://localhost:8095/', 
	'ROLE_READ_DEPARTMENT', 
	180,/*3 min*/ 
	300,/*5 min*/ 
	null, 
	'true'
);

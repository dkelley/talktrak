-- insert the bsaic roles
-- viewer, assistant, editor, owner
-- owner, can manage other helpers
INSERT INTO role(role_id, description, display_order) VALUES (1, 'Owner', 1);
-- full editing
INSERT INTO role(role_id, description, display_order) VALUES (2, 'Editor', 2);
-- can create and pass notes 
INSERT INTO role(role_id, description, display_order) VALUES (3, 'Assistant', 3);

-- test123 = 'cbaeb0cc5d74a93390b27cf215492563'
INSERT INTO account (account_id, username, email, first_name, last_name, phone_number, password, remember_me_token, api_token, updated_date) VALUES(1, 'owner','owner@xmog.com', 'Dan', 'Kelley', '6103061733', 'cbaeb0cc5d74a93390b27cf215492563', '4', '4', now());
INSERT INTO account (account_id, username, email, first_name, last_name, phone_number, password, remember_me_token, api_token, updated_date) VALUES(2, 'editor','editor@xmog.com', 'Dan', 'Kelley', '6103061733', 'cbaeb0cc5d74a93390b27cf215492563', '2', '2', now());
INSERT INTO account (account_id, username, email, first_name, last_name, phone_number, password, remember_me_token, api_token, updated_date) VALUES(3, 'assistant','assistant@xmog.com', 'Dan', 'Kelley', '6103061733', 'cbaeb0cc5d74a93390b27cf215492563', '3', '3', now());


INSERT INTO trak(trak_id,title ,description, updated_date) VALUES(1, 'Trak With Collab', '<h1>Test Trak</h1>The is a <b>test trak</b>, <br/>it is just bs', now());
INSERT INTO trak(trak_id,title ,description, updated_date) VALUES(2, 'Gettysburg Address', '<b>Four score and seven years ago</b> our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal. Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. <br><br>&nbsp;We are met on a great battle-field of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this. But, in a larger sense, <u>we can not dedicate, we can not consecrate, we can not hallow this ground</u>. <br><br>The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. <br><h3>It is rather for us to be here dedicated to the great task remaining before us that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion that we here
 highly resolve that these dead shall not have died in vain <b>that this nation, under God, shall have a new birth of freedom and that government of the people, by the people, for the people, shall not perish from the earth.</b></h3>', now());

-- nextval('point_seq')
INSERT INTO point(point_id, trak_id, title, description, created_by, updated_date, sort_order) VALUES(1,1, 'Important Point', 'This is aanother point', 1, now(), 1);
INSERT INTO point(point_id, trak_id, title, description, created_by, updated_date, sort_order) VALUES(2,1, 'Great Point', 'This is the final point', 2, now(), 2);

INSERT INTO point(point_id, trak_id, title, description, created_by, updated_date, sort_order) VALUES(3,2, 'Why Four Score', 'This refers to referring to the Declaration of Independence, written at the start of the American Revolution in 1776, Lincoln examined the founding principles of the United States in the context of the Civil War, and memorialized the sacrifices of those who gave their lives at Gettysburg and extolled virtues for the listeners (and the nation) to ensure the survival of America&quot;s representative democracy, that "government of the people, by the people, for the people, shall not perish from the earth."', 1, now(), 1);
INSERT INTO point(point_id, trak_id, title, description, created_by, updated_date, sort_order) VALUES(4,2, 'Most Famous by Lincoln', 'On June 1, 1865, Senator Charles Sumner commented on what is now considered the most famous speech by President Abraham Lincoln. In his eulogy on the slain president, he called it a "monumental act." He said Lincoln was mistaken that "the world will little note, nor long remember what we say here." Rather, the Bostonian remarked, "The world noted at once what he said, and will never cease to remember it. The battle itself was less important than the speech."', 2, now(), 2);

INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(1,1,1);
INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(2,2,1);
INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(3,3,1);

INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(2,1,2);
INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(1,2,2);
INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES(3,3,2);

-- nextval('highlighted_point_seq')
INSERT INTO highlighted_point(highlighted_point_id, point_id, account_id) VALUES(1, 1, 1);
INSERT INTO highlighted_point(highlighted_point_id, point_id, account_id) VALUES(2, 4, 1);
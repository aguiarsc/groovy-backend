-- Users (password is 'password' encoded with BCrypt)
INSERT INTO users (id, name, email, password, role, dtype) VALUES 
(1, 'Admin', 'admin@example.com', '{noop}Snbum49C-', 'ADMIN', 'User'), 
(2, 'User1', 'user1@example.com', '{noop}Snbum48C-', 'USER', 'User'), 
(3, 'User2', 'user2@example.com', '{noop}Snbum47C-', 'USER', 'User'); 

-- Artists (extending users)
INSERT INTO users (id, name, email, password, role, dtype, biography, profile_picture) VALUES 
(4, 'Snoop Dogg', 'snoop-dogg@example.com', '{noop}Snbum46C-', 'ARTIST', 'Artist', 'Snoop Dogg, born Calvin Cordozar Broadus Jr., is a legendary rapper, actor, and entrepreneur. Rising to fame in the early 1990s under the mentorship of Dr. Dre, he introduced the world to his smooth flow and laid-back delivery on his debut album, Doggystyle (1993). With hits like "Gin and Juice" and "Who Am I (Whats My Name)?", he cemented his status as a West Coast hip-hop icon. Over the years, Snoop has expanded his influence beyond music, venturing into film, television, business, and even esports.', 'artist1.jpg'), 
(5, 'Kanye West', 'kanye-west@example.com', '{noop}Snbum45C-', 'ARTIST', 'Artist', 'Kanye West is a pioneering rapper, producer, and fashion designer whose work has continuously pushed the boundaries of hip-hop. Starting as a producer for Jay-Z, he gained worldwide recognition with his debut album, The College Dropout (2004). With critically acclaimed projects like My Beautiful Dark Twisted Fantasy and Yeezus, Kanye has consistently reinvented his sound. Apart from music, he has left a lasting impact on the fashion industry with his Yeezy brand, and he remains one of the most influential and controversial figures in entertainment.', 'artist2.jpg'), 
(6, 'Ice Cube', 'ice-cube@example.com', '{noop}Snbum44C-', 'ARTIST', 'Artist', 'Ice Cube, born OShea Jackson Sr., is a legendary rapper, actor, and filmmaker known for his socially conscious and politically charged lyrics. As a member of N.W.A., he played a crucial role in bringing gangsta rap to the mainstream with the iconic album, Straight Outta Compton (1988). His solo career further solidified his influence with classic albums like AmeriKKKas Most Wanted. Beyond music, Cube has found success in Hollywood, starring in and producing hit films like Friday, Boyz n the Hood, and the Barbershop series.', 'artist3.jpg'), 
(7, 'Dr. Dre', 'dr-dre@example.com', '{noop}Snbum43C-', 'ARTIST', 'Artist', 'Dr. Dre, born Andre Young, is a legendary rapper, producer, and entrepreneur who shaped West Coast hip-hop. As a member of N.W.A., he pioneered gangsta rap before launching a highly successful solo career with The Chronic (1992), which defined the G-funk sound. Beyond his own music, Dre played a pivotal role in launching the careers of artists like Snoop Dogg, Eminem, and 50 Cent. He also co-founded Beats by Dre, later selling it to Apple in a billion-dollar deal, further cementing his legacy in both music and business.', 'artist4.jpg'), 
(8, 'Eminem', 'eminem@example.com', '{noop}Snbum42C-', 'ARTIST', 'Artist', 'Eminem, born Marshall Bruce Mathers III, is one of the most successful and technically skilled rappers of all time. He rose to fame with The Slim Shady LP (1999), introducing his controversial alter ego. With The Marshall Mathers LP and The Eminem Show, he became a global superstar, known for his intricate rhyme schemes and deeply personal lyrics. His hit song "Lose Yourself" earned him an Academy Award, and he has won multiple Grammys, solidifying his place as one of hip-hops greatest storytellers.', 'artist5.jpg'), 
(9, 'Rakim', 'rakim@example.com', '{noop}Snbum41C-', 'ARTIST', 'Artist', 'Rakim, widely regarded as one of the greatest MCs of all time, revolutionized hip-hop with his intricate rhyme patterns and smooth flow. As one half of the duo Eric B. & Rakim, he set a new lyrical standard with classics like Paid in Full (1987) and Follow the Leader (1988). His complex internal rhymes and effortless delivery influenced generations of rappers, making him one of the most respected figures in hip-hop history.', 'artist6.jpg'), 
(10, 'Nas', 'nas@example.com', '{noop}Snbum39C-', 'ARTIST', 'Artist', 'Nas, born Nasir Jones, is one of the most revered lyricists in hip-hop, known for his poetic storytelling and social commentary. His debut album, Illmatic (1994), is widely considered one of the greatest hip-hop albums of all time. Over the years, he has maintained his lyrical prowess, releasing critically acclaimed projects like Stillmatic and Kings Disease. Beyond music, Nas has ventured into business and philanthropy, solidifying his impact beyond the mic.', 'artist7.jpg'),
(11, 'Canserbero', 'canserbero@example.com', '{noop}Snbum38C-', 'ARTIST', 'Artist', 'Canserbero, born Tyrone González, was a Venezuelan rapper and lyricist known for his deep, introspective lyrics and powerful social commentary. With albums like Vida (2010) and Muerte (2012), he became a voice for truth and resistance in Latin American hip-hop. His raw storytelling and philosophical approach to themes of life, death, and injustice earned him a devoted following. Despite his tragic passing in 2015, Canserberos music continues to inspire and influence generations of artists and fans alike.', 'artist8.jpg'),
(12, 'Machine Gun Kelly', 'machine-gun-kelly@example.com', '{noop}Snbum37C-', 'ARTIST', 'Artist', 'Machine Gun Kelly, born Jason M. B. Ray, is a rapper, singer, songwriter, and actor known for his powerful vocals and aggressive style. With his debut album, The Heart of the Matter (2010), he gained recognition for his raw, unapologetic lyrics and his ability to blend rap with R&B. His hit song "Telling the World" earned him a Grammy nomination, and he has since released critically acclaimed projects like The Heart of the Matter and The Heart of the Matter 2. Beyond music, Kelly has ventured into acting, starring in films like The Heart of the Matter and The Heart of the Matter 2.', 'artist9.jpg');
-- Albums
INSERT INTO albums (id, name, artist_id, cover_image) VALUES 
(1, 'Tha Doggfather', 4, 'album1.jpg'),
(2, 'Graduation', 5, 'album2.jpg'),
(3, 'Death Certificate', 6, 'album3.jpg'),
(4, 'Compton', 7, 'album4.jpg'),
(5, 'The Marshall Mathers LP', 8, 'album5.jpg'),
(6, 'Illmatic', 10, 'album6.jpg'),
(7, 'Paid in Full', 9, 'album7.jpg'),
(8, 'Vida', 11, 'album8.jpg'),
(9, 'Muerte', 11, 'album9.jpg'),
(10, 'The Matter', 12, 'album10.jpg');

-- Songs
INSERT INTO songs (id, title, duration, file_path, album_id) VALUES 
(1, 'Just Dippin', 312, 'just-dippin_snoop-dogg.mp3', 1),
(2, 'Mercy', 312, 'mercy_kanye-west.mp3', 2),
(3, 'No Vaseline', 300, 'no-vaseline_ice-cube.mp3', 3),
(4, 'Still D.R.E.', 292, 'still-dre_dr-dre.mp3', 4),
(5, 'Rap God', 284, 'rap-god_eminem.mp3', 5),
(6, 'N.Y. State of Mind', 292, 'ny-state-of-mind_nas.mp3', 6),
(7, 'When I B on the mic', 321, 'when-i-b-on-the-mic_rakim.mp3', 7),
(8, 'Killshot', 321, 'killshot_eminem.mp3', 5),
(9, 'Houdini', 288, 'houdini_eminem.mp3', 5),
(10, 'Gin and Juice', 305, 'gin-and-juice_snoop-dogg.mp3', 1),
(11, 'Who Am I', 289, 'who-am-i_snoop-dogg.mp3', 1),
(12, 'Niggas in Paris', 321, 'niggas-in-paris_kanye-west.mp3', 2),
(13, 'Famous', 321, 'famous_kanye-west.mp3', 2),
(14, 'Forgot About Dre', 312, 'forgot-about-dre_dr-dre.mp3', 4),
(15, 'The Next Episode', 292, 'the-next-episode_dr-dre.mp3', 4),
(16, 'It was a good day', 321, 'it-was-a-good-day_ice-cube.mp3', 3),
(17, 'Gangsta Rap made me do it', 321, 'gangsta-rap-made-me-do-it_ice-cube.mp3', 3),
(18, 'Est la mort', 321, 'est-la-mort_canserbero.mp3', 9),
(19, 'Es epico', 321, 'es-epico_canserbero.mp3', 9),
(20, 'El primer trago', 321, 'el-primer-trago_canserbero.mp3', 9),
(21, 'Jeremias 17:12', 400, 'jeremias-17-12_canserbero.mp3', 9),
(22, 'Llovia', 299, 'llovia_canserbero.mp3', 8),
(23, 'Maquiavelico', 376, 'maquiavelico_canserbero.mp3', 9),
(24, 'Mundo de piedra', 321, 'mundo-de-piedra_canserbero.mp3', 8),
(25, 'Pensando en ti', 304, 'pensando-en-ti_canserbero.mp3', 9),
(26, 'Rap Devil', 321, 'rap-devil_machine-gun-kelly.mp3', 10);

-- Playlists
INSERT INTO playlists (id, name, user_id) VALUES 
(1, 'West Coast Vibes', 2),
(2, 'Beats and Rhymes', 2),
(3, 'Golden Era', 3),
(4, 'Legendary Tracks', 3);

-- Playlist Songs
INSERT INTO playlist_songs (playlist_id, song_id) VALUES 
(1, 1), (1, 4), (1, 5),
(2, 2), (2, 6), (2, 7),
(3, 3), (3, 6), (3, 7),
(4, 1), (4, 2), (4, 3),
(4, 5), (4, 8), (4, 9),
(1, 10), (1, 11),(2, 12),
(2, 13),(1, 14), (1, 15),
(3, 16), (3, 17);

-- User Favorites
INSERT INTO user_favorites (user_id, song_id) VALUES
(2, 1),  -- User1 likes "Just Dippin"
(2, 5),  -- User1 likes "Rap God"
(3, 3),  -- User2 likes "No Vaseline"
(3, 6),  -- User2 likes "N.Y. State of Mind"
(2, 10), -- User1 likes "Gin and Juice"
(3, 15), -- User2 likes "The Next Episode"
(3, 23); -- User2 likes "Maquiavelico"


-- Reset ID sequences to avoid primary key conflicts
ALTER TABLE users ALTER COLUMN id RESTART WITH 14;
ALTER TABLE albums ALTER COLUMN id RESTART WITH 12;
ALTER TABLE songs ALTER COLUMN id RESTART WITH 28;
ALTER TABLE playlists ALTER COLUMN id RESTART WITH 5;
ALTER TABLE user_favorites ALTER COLUMN id RESTART WITH 8;

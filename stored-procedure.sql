DELIMITER //

DROP PROCEDURE IF EXISTS GetAllCustomers//

CREATE PROCEDURE GetAllCustomers()
BEGIN
	SELECT *  FROM customers;
END//

DROP PROCEDURE IF EXISTS GetAllEmployees//
CREATE PROCEDURE GetAllEmployees()
BEGIN
	SELECT *  FROM employees;
END//

DROP PROCEDURE IF EXISTS add_star//
CREATE PROCEDURE add_star(
	IN astarname varchar(100),
    IN astarbirthyear int,
    OUT rsuccess int,
    OUT rstarid VARCHAR(10)
)
BEGIN
-- star portion
    DECLARE maxStarID VARCHAR(10);
    DECLARE StarIDAlpa VARCHAR(10);
    DECLARE StarIDInt int;
    DECLARE NextStarID VARCHAR(10);
    
    -- temp table for star
    DECLARE tstartable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    
    

    
    


    
    -- check if star exist
	-- IF (SELECT NOT EXISTS (SELECT strs.id FROM stars AS strs WHERE strs.name=astarname AND strs.birthYear=astarbirthyear)) THEN
		CALL sys.table_exists('moviedb', 'tstorestar', @tstarexists);
		SELECT @tstarexists into tstartable;
	
		IF (tstartable='') THEN
			-- temp table does not exist
			CREATE temporary TABLE IF NOT EXISTS tstorestar(
			prefix VARCHAR(10),
			num int DEFAULT NULL,
			PRIMARY KEY(prefix)
			);
			
			SELECT MAX(strs.id) INTO maxStarID FROM stars as strs;

			-- star does not exist store next star id
			SET StarIDAlpa = SUBSTRING(maxStarID,1,2);
			SET StarIDInt = CAST(SUBSTRING(maxStarID,3) AS UNSIGNED);
			SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
			
			INSERT INTO tstorestar (prefix,num)
			VALUES
			(StarIDAlpa,StarIDInt + 1);
			
			
		ELSE
			SELECT prefix,num INTO StarIDAlpa,StarIDInt FROM tstorestar;
			SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
			
			UPDATE tstorestar
			SET num = StarIDInt + 1
			WHERE prefix=StarIDAlpa;
		END IF;
        
    
		
		
		-- insert into stars
		INSERT INTO stars (id,name,birthYear)
		VALUES
		(NextStarID,astarname,astarbirthyear);
        
        -- set return variables
        SET rsuccess = 1;
        SET rstarid = NextStarID;
-- 	ELSE
-- 		-- set return variables
--         SET rsuccess = 0;

-- 	END IF;
    
    
    


END//

DROP PROCEDURE IF EXISTS add_movie//
-- movie title, director,year,genre,starname,starbirthyear(not required)
CREATE PROCEDURE add_movie(
	IN atitle varchar(100),
    IN ayear int,
    IN adirector varchar(100),
    IN astarname varchar(100),
    IN astarbirthyear int,
    IN agenrename varchar(32),
    OUT rsuccess int,
    OUT rmovieid VARCHAR(10),
    OUT rstarid VARCHAR(10),
    OUT rgenreid int
)
BEGIN
	-- movie portion
	DECLARE maxMovieID VARCHAR(10);
    DECLARE MovieIDAlpa VARCHAR(10);
    DECLARE MovieIDInt int;
    DECLARE NextMovieID VARCHAR(10);
    -- star portion
    DECLARE maxStarID VARCHAR(10);
    DECLARE StarIDAlpa VARCHAR(10);
    DECLARE StarIDInt int;
    DECLARE NextStarID VARCHAR(10);
    
    -- genre portion
    DECLARE maxGenreID int;
    DECLARE NextGenreID int;
    
    -- temp table for movie
    DECLARE tmovietable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
	-- temp table for star
    DECLARE tstartable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    -- temp table for genre
    DECLARE tgenretable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    
    
	
	-- first check if the movie exists
    IF (SELECT NOT EXISTS (SELECT mvie.id FROM movies as mvie WHERE mvie.title=atitle AND mvie.year=ayear AND mvie.director=adirector)) THEN
		CALL sys.table_exists('moviedb', 'tstoremovie', @tmovieexists);
		SELECT @tmovieexists into tmovietable;
	
		IF (tmovietable='') THEN
			-- temp table does not exist
			CREATE temporary TABLE IF NOT EXISTS tstoremovie(
			prefix VARCHAR(10),
			num int DEFAULT NULL,
			PRIMARY KEY(prefix)
			);
			
			
			SELECT MAX(mvie.id) INTO maxMovieID FROM movies as mvie;

			-- store next movie id into var
			SET MovieIDAlpa = SUBSTRING(maxMovieID,1,2);
			SET MovieIDInt = CAST(SUBSTRING(maxMovieID,3) AS UNSIGNED);
			SET NextMovieID = CONCAT(MovieIDAlpa,MovieIDInt + 1);
			
			INSERT INTO tstoremovie (prefix,num)
			VALUES
			(MovieIDAlpa,MovieIDInt + 1);
			
			
		ELSE
			SELECT prefix,num INTO MovieIDAlpa,MovieIDInt FROM tstoremovie;
			SET NextMovieID = CONCAT(MovieIDAlpa,MovieIDInt + 1);
			
			UPDATE tstoremovie
			SET num = MovieIDInt + 1
			WHERE prefix=MovieIDAlpa;
		END IF;
        
        
        
        
        
        
        
        
        
        -- insert into movies
        INSERT INTO movies (id,title,year,director,price)
        VALUES
        (NextMovieID,atitle,ayear,adirector,RAND()*(40-10)+10);
        
        -- check if star exist
        IF (SELECT NOT EXISTS (SELECT strs.id FROM stars AS strs WHERE strs.name=astarname)) THEN
			CALL sys.table_exists('moviedb', 'tstorestar', @tstarexists);
			SELECT @tstarexists into tstartable;
		
			IF (tstartable='') THEN
				-- temp table does not exist
				CREATE temporary TABLE IF NOT EXISTS tstorestar(
				prefix VARCHAR(10),
				num int DEFAULT NULL,
				PRIMARY KEY(prefix)
				);
				
				SELECT MAX(strs.id) INTO maxStarID FROM stars as strs;

				-- star does not exist store next star id
				SET StarIDAlpa = SUBSTRING(maxStarID,1,2);
				SET StarIDInt = CAST(SUBSTRING(maxStarID,3) AS UNSIGNED);
				SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
				
				INSERT INTO tstorestar (prefix,num)
				VALUES
				(StarIDAlpa,StarIDInt + 1);
				
				
			ELSE
				SELECT prefix,num INTO StarIDAlpa,StarIDInt FROM tstorestar;
				SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
				
				UPDATE tstorestar
				SET num = StarIDInt + 1
				WHERE prefix=StarIDAlpa;
			END IF;

        
            
            -- insert into stars
            INSERT INTO stars (id,name,birthYear)
            VALUES
            (NextStarID,astarname,astarbirthyear);
            
		ELSE
			-- if star exists then just store the id
			SELECT strs.id INTO NextStarID FROM stars as strs WHERE strs.name=astarname LIMIT 1;
            
        END IF;
        
        -- add to stars_in_movies table
        INSERT INTO stars_in_movies (starId,movieId)
        VALUES
        (NextStarID,NextMovieID);
        
        
        -- check if genre exists
        IF (SELECT NOT EXISTS (SELECT gnre.id FROM genres AS gnre WHERE gnre.name=agenrename)) THEN
			CALL sys.table_exists('moviedb', 'tstoregenre', @tgenreexists);
			SELECT @tgenreexists into tgenretable;
		
			IF (tgenretable='') THEN
				-- temp table does not exist
				CREATE temporary TABLE IF NOT EXISTS tstoregenre(
				prefix VARCHAR(10),
				num int DEFAULT NULL,
				PRIMARY KEY(prefix)
				);
				
				
                -- genre does not exist must create one
				SELECT MAX(gnre.id) INTO maxGenreID FROM genres AS gnre;

				-- genre does not exist store next genre id
				SET NextGenreID = maxGenreID + 1;
				
				INSERT INTO tstoregenre (prefix,num)
				VALUES
				('gid',maxGenreID + 1);
				
				
			ELSE
				SELECT num INTO maxGenreID FROM tstoregenre;
				SET NextGenreID = maxGenreID + 1;
				
				UPDATE tstoregenre
				SET num = maxGenreID + 1
				WHERE prefix='gid';
			END IF;
        
        
        
			
            
            
            
            -- insert a new genre
            INSERT INTO genres (id,name)
            VALUES
            (NextGenreID,agenrename);
            
		ELSE
			-- genre already exist just grab genre id
            SELECT gnre.id INTO NextGenreID FROM genres as gnre WHERE gnre.name=agenrename LIMIT 1;
            
        END IF;
        
        -- insert into genres_in_movies
        INSERT INTO genres_in_movies (genreId,movieId)
        VALUES
        (NextGenreID,NextMovieID);
        
        
        
        -- set the returns vars
        SET rsuccess = 1;
        SET rmovieid = NextMovieID;
        SET rstarid = NextStarID;
        SET rgenreid = NextGenreID;
        
        
    ELSE
		-- set the return var to fail
		SET rsuccess = 0;
    END IF;
	-- SELECT *  FROM employees;
END//

DROP PROCEDURE IF EXISTS add_moviept6//
-- movie title, director,year,genre,starname,starbirthyear(not required)
CREATE PROCEDURE add_moviept6(
	IN atitle varchar(100),
    IN ayear int,
    IN adirector varchar(100),
    IN astarname varchar(100),
    IN astarbirthyear int,
    IN agenrename varchar(32),
    OUT rsuccess int,
    OUT rmovieid VARCHAR(10),
    OUT rstarid VARCHAR(10),
    OUT rgenreid int
)
BEGIN
	-- movie portion
	DECLARE maxMovieID VARCHAR(10);
    DECLARE MovieIDAlpa VARCHAR(10);
    DECLARE MovieIDInt int;
    DECLARE NextMovieID VARCHAR(10);
    -- star portion
    DECLARE maxStarID VARCHAR(10);
    DECLARE StarIDAlpa VARCHAR(10);
    DECLARE StarIDInt int;
    DECLARE NextStarID VARCHAR(10);
    
    -- genre portion
    DECLARE maxGenreID int;
    DECLARE NextGenreID int;
    
    -- temp table for movie
    DECLARE tmovietable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
	-- temp table for star
    DECLARE tstartable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    -- temp table for genre
    DECLARE tgenretable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    
    
	
	-- first check if the movie exists
    IF (SELECT NOT EXISTS (SELECT mvie.id FROM movies as mvie WHERE mvie.title=atitle AND mvie.year=ayear AND mvie.director=adirector)) THEN
		CALL sys.table_exists('moviedb', 'tstoremovie', @tmovieexists);
		SELECT @tmovieexists into tmovietable;
	
		IF (tmovietable='') THEN
			-- temp table does not exist
			CREATE temporary TABLE IF NOT EXISTS tstoremovie(
			prefix VARCHAR(10),
			num int DEFAULT NULL,
			PRIMARY KEY(prefix)
			);
			
			
			SELECT MAX(mvie.id) INTO maxMovieID FROM movies as mvie;

			-- store next movie id into var
			SET MovieIDAlpa = SUBSTRING(maxMovieID,1,2);
			SET MovieIDInt = CAST(SUBSTRING(maxMovieID,3) AS UNSIGNED);
			SET NextMovieID = CONCAT(MovieIDAlpa,MovieIDInt + 1);
			
			INSERT INTO tstoremovie (prefix,num)
			VALUES
			(MovieIDAlpa,MovieIDInt + 1);
			
			
		ELSE
			SELECT prefix,num INTO MovieIDAlpa,MovieIDInt FROM tstoremovie;
			SET NextMovieID = CONCAT(MovieIDAlpa,MovieIDInt + 1);
			
			UPDATE tstoremovie
			SET num = MovieIDInt + 1
			WHERE prefix=MovieIDAlpa;
		END IF;
        
        
        
        
        
        
        
        
        
        -- insert into movies
        INSERT INTO movies (id,title,year,director,price)
        VALUES
        (NextMovieID,atitle,ayear,adirector,RAND()*(40-10)+10);
        
        -- check if star exist
        IF (SELECT NOT EXISTS (SELECT strs.id FROM stars AS strs WHERE strs.name=astarname)) THEN
			CALL sys.table_exists('moviedb', 'tstorestar', @tstarexists);
			SELECT @tstarexists into tstartable;
		
			IF (tstartable='') THEN
				-- temp table does not exist
				CREATE temporary TABLE IF NOT EXISTS tstorestar(
				prefix VARCHAR(10),
				num int DEFAULT NULL,
				PRIMARY KEY(prefix)
				);
				
				SELECT MAX(strs.id) INTO maxStarID FROM stars as strs;

				-- star does not exist store next star id
				SET StarIDAlpa = SUBSTRING(maxStarID,1,2);
				SET StarIDInt = CAST(SUBSTRING(maxStarID,3) AS UNSIGNED);
				SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
				
				INSERT INTO tstorestar (prefix,num)
				VALUES
				(StarIDAlpa,StarIDInt + 1);
				
				
			ELSE
				SELECT prefix,num INTO StarIDAlpa,StarIDInt FROM tstorestar;
				SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
				
				UPDATE tstorestar
				SET num = StarIDInt + 1
				WHERE prefix=StarIDAlpa;
			END IF;

        
            
            -- insert into stars
            INSERT INTO stars (id,name,birthYear)
            VALUES
            (NextStarID,astarname,astarbirthyear);
            
		ELSE
			-- if star exists then just store the id
			SELECT strs.id INTO NextStarID FROM stars as strs WHERE strs.name=astarname LIMIT 1;
            
        END IF;
        
        -- add to stars_in_movies table
        INSERT INTO stars_in_movies (starId,movieId)
        VALUES
        (NextStarID,NextMovieID);
        
        
        -- check if genre exists
        IF (SELECT NOT EXISTS (SELECT gnre.id FROM genres AS gnre WHERE gnre.name=agenrename)) THEN
			CALL sys.table_exists('moviedb', 'tstoregenre', @tgenreexists);
			SELECT @tgenreexists into tgenretable;
		
			IF (tgenretable='') THEN
				-- temp table does not exist
				CREATE temporary TABLE IF NOT EXISTS tstoregenre(
				prefix VARCHAR(10),
				num int DEFAULT NULL,
				PRIMARY KEY(prefix)
				);
				
				
                -- genre does not exist must create one
				SELECT MAX(gnre.id) INTO maxGenreID FROM genres AS gnre;

				-- genre does not exist store next genre id
				SET NextGenreID = maxGenreID + 1;
				
				INSERT INTO tstoregenre (prefix,num)
				VALUES
				('gid',maxGenreID + 1);
				
				
			ELSE
				SELECT num INTO maxGenreID FROM tstoregenre;
				SET NextGenreID = maxGenreID + 1;
				
				UPDATE tstoregenre
				SET num = maxGenreID + 1
				WHERE prefix='gid';
			END IF;
        
        
        
			
            
            
            
            -- insert a new genre
            INSERT INTO genres (id,name)
            VALUES
            (NextGenreID,agenrename);
            
		ELSE
			-- genre already exist just grab genre id
            SELECT gnre.id INTO NextGenreID FROM genres as gnre WHERE gnre.name=agenrename LIMIT 1;
            
        END IF;
        
        -- insert into genres_in_movies
        INSERT INTO genres_in_movies (genreId,movieId)
        VALUES
        (NextGenreID,NextMovieID);
        
        
        
        -- set the returns vars
        SET rsuccess = 1;
        SET rmovieid = NextMovieID;
        SET rstarid = NextStarID;
        SET rgenreid = NextGenreID;
        
        
    ELSE
		-- set the return var to fail
		SET rsuccess = 0;
        
        -- store the existing movie id
        SELECT mvie.id INTO NextMovieID FROM movies as mvie WHERE mvie.title=atitle AND mvie.year=ayear AND mvie.director=adirector LIMIT 1;
        
        
        -- check if genre exists
        IF (SELECT NOT EXISTS (SELECT gnre.id FROM genres AS gnre WHERE gnre.name=agenrename)) THEN
			CALL sys.table_exists('moviedb', 'tstoregenre', @tgenreexists);
			SELECT @tgenreexists into tgenretable;
		
			IF (tgenretable='') THEN
				-- temp table does not exist
				CREATE temporary TABLE IF NOT EXISTS tstoregenre(
				prefix VARCHAR(10),
				num int DEFAULT NULL,
				PRIMARY KEY(prefix)
				);
				
				
                -- genre does not exist must create one
				SELECT MAX(gnre.id) INTO maxGenreID FROM genres AS gnre;

				-- genre does not exist store next genre id
				SET NextGenreID = maxGenreID + 1;
				
				INSERT INTO tstoregenre (prefix,num)
				VALUES
				('gid',maxGenreID + 1);
				
				
			ELSE
				SELECT num INTO maxGenreID FROM tstoregenre;
				SET NextGenreID = maxGenreID + 1;
				
				UPDATE tstoregenre
				SET num = maxGenreID + 1
				WHERE prefix='gid';
			END IF;
        
        
        
			
            
            
            
            -- insert a new genre
            INSERT INTO genres (id,name)
            VALUES
            (NextGenreID,agenrename);
            
		ELSE
			-- genre already exist just grab genre id
            SELECT gnre.id INTO NextGenreID FROM genres as gnre WHERE gnre.name=agenrename LIMIT 1;
            
        END IF;
        
        -- need to check if it exists in gim
        
        IF (SELECT NOT EXISTS (SELECT gim.genreId FROM genres_in_movies as gim WHERE gim.genreId=NextGenreID AND gim.movieId=NextMovieID LIMIT 1)) THEN
        
			-- insert into genres_in_movies
			INSERT INTO genres_in_movies (genreId,movieId)
			VALUES
			(NextGenreID,NextMovieID);
        
        END IF;
        
        -- check if star exist
        IF (SELECT NOT EXISTS (SELECT strs.id FROM stars AS strs WHERE strs.name=astarname)) THEN
			CALL sys.table_exists('moviedb', 'tstorestar', @tstarexists);
			SELECT @tstarexists into tstartable;
		
			IF (tstartable='') THEN
				-- temp table does not exist
				CREATE temporary TABLE IF NOT EXISTS tstorestar(
				prefix VARCHAR(10),
				num int DEFAULT NULL,
				PRIMARY KEY(prefix)
				);
				
				SELECT MAX(strs.id) INTO maxStarID FROM stars as strs;

				-- star does not exist store next star id
				SET StarIDAlpa = SUBSTRING(maxStarID,1,2);
				SET StarIDInt = CAST(SUBSTRING(maxStarID,3) AS UNSIGNED);
				SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
				
				INSERT INTO tstorestar (prefix,num)
				VALUES
				(StarIDAlpa,StarIDInt + 1);
				
				
			ELSE
				SELECT prefix,num INTO StarIDAlpa,StarIDInt FROM tstorestar;
				SET NextStarID = CONCAT(StarIDAlpa,StarIDInt + 1);
				
				UPDATE tstorestar
				SET num = StarIDInt + 1
				WHERE prefix=StarIDAlpa;
			END IF;

        
            
            -- insert into stars
            INSERT INTO stars (id,name,birthYear)
            VALUES
            (NextStarID,astarname,astarbirthyear);
            
		ELSE
			-- if star exists then just store the id
			SELECT strs.id INTO NextStarID FROM stars as strs WHERE strs.name=astarname LIMIT 1;
            
        END IF;
        
        -- need to check if it exists in sim
        
        IF (SELECT NOT EXISTS (SELECT sim.starId FROM stars_in_movies as sim WHERE sim.starId=NextStarID AND sim.movieId=NextMovieID LIMIT 1)) THEN
        
			-- add to stars_in_movies table
			INSERT INTO stars_in_movies (starId,movieId)
			VALUES
			(NextStarID,NextMovieID);
        
        END IF;
        
        
        
        
        
        
    END IF;
	-- SELECT *  FROM employees;
END//


DROP PROCEDURE IF EXISTS add_singlemoviept6//
-- movie title, director,year,genre,starname,starbirthyear(not required)
CREATE PROCEDURE add_singlemoviept6(
	IN atitle varchar(100),
    IN ayear int,
    IN adirector varchar(100),
    OUT rsuccess int,
    OUT rmovieid VARCHAR(10)
)
BEGIN
	-- movie portion
	DECLARE maxMovieID VARCHAR(10);
    DECLARE MovieIDAlpa VARCHAR(10);
    DECLARE MovieIDInt int;
    DECLARE NextMovieID VARCHAR(10);
    -- star portion
    DECLARE maxStarID VARCHAR(10);
    DECLARE StarIDAlpa VARCHAR(10);
    DECLARE StarIDInt int;
    DECLARE NextStarID VARCHAR(10);
    
    -- genre portion
    DECLARE maxGenreID int;
    DECLARE NextGenreID int;
    
    -- temp table for movie
    DECLARE tmovietable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
	-- temp table for star
    DECLARE tstartable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    -- temp table for genre
    DECLARE tgenretable ENUM('', 'BASE TABLE', 'VIEW', 'TEMPORARY');
    
    
    
	
	-- first check if the movie exists
    IF (SELECT NOT EXISTS (SELECT mvie.id FROM movies as mvie WHERE mvie.title=atitle AND mvie.year=ayear AND mvie.director=adirector)) THEN
		CALL sys.table_exists('moviedb', 'tstoremovie', @tmovieexists);
		SELECT @tmovieexists into tmovietable;
	
		IF (tmovietable='') THEN
			-- temp table does not exist
			CREATE temporary TABLE IF NOT EXISTS tstoremovie(
			prefix VARCHAR(10),
			num int DEFAULT NULL,
			PRIMARY KEY(prefix)
			);
			
			
			SELECT MAX(mvie.id) INTO maxMovieID FROM movies as mvie;

			-- store next movie id into var
			SET MovieIDAlpa = SUBSTRING(maxMovieID,1,2);
			SET MovieIDInt = CAST(SUBSTRING(maxMovieID,3) AS UNSIGNED);
			SET NextMovieID = CONCAT(MovieIDAlpa,MovieIDInt + 1);
			
			INSERT INTO tstoremovie (prefix,num)
			VALUES
			(MovieIDAlpa,MovieIDInt + 1);
			
			
		ELSE
			SELECT prefix,num INTO MovieIDAlpa,MovieIDInt FROM tstoremovie;
			SET NextMovieID = CONCAT(MovieIDAlpa,MovieIDInt + 1);
			
			UPDATE tstoremovie
			SET num = MovieIDInt + 1
			WHERE prefix=MovieIDAlpa;
		END IF;
        
        
        
        
        
        
        
        
        
        -- insert into movies
        INSERT INTO movies (id,title,year,director,price)
        VALUES
        (NextMovieID,atitle,ayear,adirector,RAND()*(40-10)+10);
        
       
            
		
        
       
        
        
        
        -- set the returns vars
        SET rsuccess = 1;
        SET rmovieid = NextMovieID;
        
        
        
    ELSE
		-- set the return var to fail
		SET rsuccess = 0;
        
        
        
        
        
        
    END IF;
	-- SELECT *  FROM employees;
END//



DELIMITER ;

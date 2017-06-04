package ru.nstu.cinema.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuhan on 04.06.17.
 */
public class CinemaStorage {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/cinema";

    //  Database credentials
    private static final String USER = "username";
    private static final String PASS = "password";

    enum databases{hall,film,session,seats}
    enum hallFields{id, name, descr, struct}
    enum filmFields{id, name, descr}
    enum sessionFields{id, session_time, hall_id,film_id, price}
    enum seatsFields{id, session_id, row,seat}

    private JSONParser parser=new JSONParser();

    private JSONObject getJSONObjectFromString(String str) throws ParseException {
        return (JSONObject)parser.parse(str);
    }

    public void addHall(Hall hall){
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s, %s ) VALUES ( ? , ? , ?)",
                    databases.hall,
                    hallFields.name,
                    hallFields.descr,
                    hallFields.struct);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, hall.getName());
            stmt.setString(2, hall.getDescription());
            stmt.setString(3, hall.getStructure().toString());
            int rs = stmt.executeUpdate(sql);


            stmt.close();
            conn.close();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ignored){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

    }

    public void addFilm(Film film){
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s ) VALUES ( ? , ? )",
                    databases.film,
                    filmFields.name,
                    filmFields.descr);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescr().toString());
            int rs = stmt.executeUpdate(sql);


            stmt.close();
            conn.close();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ignored){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

    }
      public void addSession(Session session){
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;
            sql = String.format("INSERT INTO %s ( %s, %s, %s, %s ) VALUES ( ? , ? , ? , ?  )",
                    databases.session,
                    sessionFields.session_time,
                    sessionFields.hall_id,
                    sessionFields.film_id,
                    sessionFields.price
                    );
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(session.getTime().toLocalDate()));
            stmt.setInt(2, session.getHall().getId());
            stmt.setInt(3, session.getFilm().getId());
            stmt.setInt(4, session.getPrice());
            int rs = stmt.executeUpdate(sql);


            stmt.close();
            conn.close();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ignored){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

    }


    public List<Hall> retrieveHalls(){
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Hall>result= new ArrayList<>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;

            sql = String.format("SELECT %s, %s, %s, %s FROM %s",
                    hallFields.id,
                    hallFields.name,
                    hallFields.descr,
                    hallFields.struct,
                    databases.hall);
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt( hallFields.id.name() );
                String name = rs.getString(  hallFields.name.name() );
                String descr = rs.getString( hallFields.descr.name() );
                String struct = rs.getString( hallFields.struct.name() );
                JSONObject jsonObject=getJSONObjectFromString(struct);

                result.add( new Hall(id,  name, descr, jsonObject ));

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", name: " + name);
                System.out.print(", descr: " + descr);
                System.out.println(", struct: " + struct);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;
    }
    public List<Film> retrieveFilms(){
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Film>result=new ArrayList<Film>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;

            sql = String.format("SELECT %s, %s, %s FROM %s",
                    hallFields.id,
                    hallFields.name,
                    hallFields.descr,
                    databases.film);
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt( filmFields.id.name() );
                String name = rs.getString(  filmFields.name.name() );
                String descr = rs.getString( filmFields.descr.name() );
                JSONObject jsonObject=getJSONObjectFromString(descr);

                result.add( new Film(id,  name, jsonObject ));

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", name: " + name);
                System.out.print(", descr: " + descr);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;

    }
    /*
    public List<Session> retrieveSessions(String film_id){
        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<Session>result=new ArrayList<Session>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql;

            sql = String.format("SELECT %s, %s, %s, %s " +
                            " FROM %s " +
                            " WHERE %s=?",
                    sessionFields.id,
                    sessionFields.session_time,
                    sessionFields.hall_id,
                    sessionFields.price,
                    databases.session,
                    sessionFields.film_id);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, film_id);
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt( sessionFields.id.name() );
                LocalDateTime session_time=LocalDateTime.parse(rs.getDate( sessionFields.session_time.name() ).toString() );
                String hall_id = rs.getString( sessionFields.hall_id.name() );
                int price = rs.getInt( sessionFields.price.name() );

                result.add( new Session(id,session_time, hall_id, price));

                //Display values
                System.out.print("ID: " + id);

            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return result;
    }
    public List<Seat> retrieveSeats(){

    }*/

}

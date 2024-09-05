/**
 * simple Tcp server
 * handles connections
 * creates users or deletes them
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;



public class Server {
    public static volatile ChatServer Chats = new ChatServer();
    private static volatile JSONObject users = Users();
    public static void main(String[] args){
       
        try{
            ServerSocket server = new ServerSocket(8080);
            while (true) {
                Socket client = server.accept();
                new Thread(()-> HandleClient(client)).start();;
                }
            
            


        }catch(Exception e){System.out.println(e.getMessage());System.out.println("Error while starting the server exit!");System.exit(1);}
        

     }
     private static void HandleClient(Socket client){
        StringBuilder ThreadResults = new StringBuilder();
        ThreadResults.append("=======================================" +"\nconnection : " + client.getInetAddress().getHostAddress());
        try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line ="";
                StringBuilder request = new StringBuilder();
                while ((line = reader.readLine())!= null) {
                    request.append(line).append("\n");

                }
                JSONObject json = new JSONObject(request.toString());
                String methode = "";
                String user = "";
                String pass = "";
                if (
                 (String)json.get("methode") != null &&  (String)json.get("methode") != "" &&
                 (String)json.get("username") != null && (String)json.get("username") != "" &&
                 (String)json.get("password")!= null && (String)json.get("password") != ""  ){
                    methode= (String)json.get("methode") ;
                    user = (String)json.get("username");
                    pass = (String)json.get("password");
                    ThreadResults.append("\nmethode : "+methode+"\nuser : " + user + "\npassword : "+pass);
                 }else {client.getOutputStream().write("Bad request ! you should send a  (methode : login or sign up or delete ) and (user : the account username) and (password : the password) with json)".getBytes());client.close();}

                if (methode.equals("Sign up")){
                    
                        if(Users().has(user)){
                            client.getOutputStream().write("user name already taken".getBytes());client.close();
                            ThreadResults.append("\nStatus : failed user auth");
                        }else {
                        ManageUsers(0,user,pass);
                        client.getOutputStream().write("successfully signed up".getBytes());
                        Chats.ConnectClient(client, user);
                        ThreadResults.append("\nStatus : success sign up");
                        }

                        
                }if (methode.equals("login")){
                    
                        if(Users().has(user)){
                            if (Users().get(user).equals(pass)){
                                client.getOutputStream().write("successfully logged in".getBytes());
                                ThreadResults.append("Status : success log in");
                                Chats.ConnectClient(client, user);
                            }else{client.getOutputStream().write("Wrong password".getBytes());client.close();ThreadResults.append("\nStatus : failed user auth");}
                        }else {

                        }

                    }


            }catch(Exception e){}
        System.out.println(ThreadResults);
     }
     public static JSONObject Users(){
        StringBuilder res = new StringBuilder() ;
        try {
            File f = new File("src/users.json");
            BufferedReader r =new BufferedReader(new FileReader(f));
            String l ;
            while ((l = r.readLine()) != null) {
            res.append(l).append("\n");
            }r.close();
        }catch(Exception e){
        return null;
    }
        return new JSONObject(res.toString());
}

    private static void ManageUsers(int methode,String user,String password){
        
        switch (methode) {
            case 0:
                users.put(user, password);
                break;
            case 1:
                users.remove(user);
                break;
        
        }
        try {
            FileOutputStream fout = new FileOutputStream(new File("users.json"));
            OutputStreamWriter writer = new OutputStreamWriter(fout, "UTF-8");
            writer.write(users.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        

    }
   
}
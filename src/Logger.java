import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Logger
{
    String filename;

    public Logger( )
    {
        this( "Logger_Output.txt" );
    }
    public Logger( String filename )
    {
        this.filename = filename;
    }

    public void Log( String line )
    {
        String time = java.time.LocalDateTime.now().toString();
        String output = time + "\t" + line + "\n";
        new Thread( new LoggerAsync( filename, output ) ).start();
    }

    public void LogMessage( Message message )
    {
        String username = message.getSender().getUsername();
        String[] recievers = new String[message.getReceivers().size()];
        for( int i = 0; i < recievers.length; i++ )
        {
            recievers[i] = message.getReceivers().get(i).getUsername();
        }
        String text = message.getText();
        String line = "Message by " + username + " to " + recievers.toString() + ": " + text;
        Log( line );
    }

    public void LogConnect( String username, String address )
    {
        String line = username + " (" + address + ") connected to the server.";
        Log( line );
    }

    public void LogDisconnect( String username, String address )
    {
        String line = username + " (" + address + ") disconnected from the server.";
        Log( line );
    }

    private class LoggerAsync implements Runnable
    {
        String filename;
        String line;

        public LoggerAsync( String filename, String line )
        {
            this.filename = filename;
            this.line = line;
        }

        @Override
        public void run( )
        {
            try
            {
                File file = new File( filename );
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream( filename, true );
                outputStream.write( line.getBytes() );
                outputStream.close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Logger logger = new Logger( );
        logger.Log( "Hello" );
    }
}

import java.io.*;
import java.util.*;

public class LoLAudioFinder {

    private static final String audFilePath = "RADS\\projects\\lol_game_client_en_us\\managedfiles\\";
    
    public static void main( String args[] ) {
        if( args.length < 2 ) {
            System.out.println();
            System.out.println( "Error: Please specify champion name and path to LoL ");
            System.out.println();
            System.out.println( "Usage:");
            System.out.println( "java LoLAudioFinder [Champion Name] [Path to LoL]");
            System.out.println();
            System.out.println( "Example:");
            System.out.println( "java LoLAudioFinder LeeSin C:\\Games\\Riot Games\\");
            System.exit(-1);
        }

        String championName = args[0];
        String lolPathName = args[1];

        for( int i = 2; i < args.length; i++ ) {
            lolPathName += " " + args[i];
        }

        /*System.out.println(championName);
        System.out.println(lolPathName);
        System.out.println(lolPathName + "\\" + audFilePath);*/

        try {      
            PathFinder finder = new PathFinder(args[1] + "\\" + audFilePath);
            printAudFiles( finder.findFile( args[0] ) );
        } catch (Exception e) {
            //uh oh
        }

    }

    public static void printAudFiles( ArrayList<String> files ) {

        for( int i = 0; i < files.size(); i++ ) {
            System.out.printf( "\nAUD FILE #%d:\n", i+1);
            System.out.println(" " + files.get(i));
        }

        if( files.size() == 0 ) {
            System.out.println("\nNo audio files found");
        }
        
    }

}

class PathFinder {

    private File dir;

    public PathFinder() {
        this.dir = new File("./");
    }

    public PathFinder(String dirName) throws Exception{
        this.dir = new File(dirName);

        if(!dir.isDirectory()) {
            throw new Exception("Error: improper directory specified for LoL files");
        }
    }
    
    public ArrayList<String> findFile ( String name ) {
        String[] files = dir.list();
        ArrayList<String> paths = new ArrayList<String>();

        for( int i = 0; i < files.length; i++ ) {
            recursiveFindFile( dir.getPath() + "\\" + files[i], name, paths);
        }

        return paths;
    }

    private void recursiveFindFile ( String fileName, String nameToFind, ArrayList<String> list ) {
        File pathQuery = new File( fileName );

        if( !pathQuery.isDirectory() ) {
            return;
        }

        String[] files = pathQuery.list();

        for( int i = 0; i < files.length; i++ ) {
            String queryFileName = files[i];
            String queryPathName = pathQuery.getPath()  + "\\" + queryFileName;
            File queryFile = new File(queryPathName);
            
            // Base case
            if( !queryFile.isDirectory() ) {
                if( stringContainsIgnoreCase(queryFileName, nameToFind) ) {
                    list.add(queryPathName);
                    return;
                }
            } else {
                // Recursive case
                recursiveFindFile(queryPathName, nameToFind, list);
            }
            
        }
    }

    private boolean stringContainsIgnoreCase( String q1, String q2 ) {
        return q1.toLowerCase().contains(q2.toLowerCase());
    }
}

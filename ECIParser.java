import java.io.*;
import java.util.*;
/**
 * Created by Paul on 4/14/2016.
 */
class Consituency{
    String name;
    int totalelectors;
    List<Candidate> candidate = new ArrayList<Candidate>();
    int generalturnout;
    int postalturnout;
    int totalturnout;
    float totalpercentage;
}
class Candidate{
    String name;
    char sex;
    int age;
    String category;
    String party;
    int generalvotes;
    int postalvotes;
    int totalvotes;
    float votepercentage;
}
public class ECIParser {
    public static void candidateDetails(File er) {
        try {
            FileReader erReader = new FileReader(er);
            LineNumberReader erLine = new LineNumberReader(erReader);
            String line;
            while ((line = erLine.readLine()) != null) {
                if(line.isEmpty()) {
                    continue;
                }
                else if(line.contains("DETAILED RESULTS")){
                    System.out.println(line);
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
    public static void main(String[] args){
        File electionresults = new File("C:/Users/Paul/Documents/results.txt");
        candidateDetails(electionresults);
    }
}

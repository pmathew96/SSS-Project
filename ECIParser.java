import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static void readResultsFromFile(File resultfile) {
        try {
            int flag = 0;
            FileReader fr = new FileReader(resultfile);
            LineNumberReader ln = new LineNumberReader(fr);
            String line;
            String candidateline = "(\\A)(\\d+)(\\s)(\\w*)";
            String constituencyline = "(\\A)(\\d+)([.])(\\s)(\\w+)";
            Pattern candidatepattern = Pattern.compile(candidateline);
            Pattern constituencypattern = Pattern.compile(constituencyline);
            while((line = ln.readLine()) != null){
                if (line.toLowerCase().contains("detailed results")){
                    flag++;
                }
                if (flag >= 2){
                    Matcher candidatematcher = candidatepattern.matcher(line);
                    Matcher constituencymatcher = constituencypattern.matcher(line);
                    if (candidatematcher.find()) {
                        System.out.println(line);
                    }
                    if (constituencymatcher.find()){
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
    public static void main(String[] args){
        File electionresults = new File("C:/Users/Paul/Documents/results.txt");
        readResultsFromFile(electionresults);
    }
}
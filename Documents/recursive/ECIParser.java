import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


/**
 * Created by Paul on 4/14/2016.
 */
class Constituency{
    String name;
    //int totalelectors;
    List<Candidate> candidates = new ArrayList<Candidate>();
    /*
    int generalturnout;
    int postalturnout;
    int totalturnout;
    float totalpercentage;
    */
}
class Candidate{
    String constituency;
    String name;
    String sex;
    String age;
    String category;
    String party;
    String general;
    String postal;
    String total;
}
public class ECIParser {
    static String newLineSeparator = "\n";
    static Object[] fileHeader = {"Constituency","Name","Sex","Age","Category","Party","General","Postal","Total"};
    /*
    Method that takes an object of type constituency and a filewriter as inputs and
    writes the constituency name and the names of all the candidates under the
    constituency into a csv file. This method is called by the readResultsFromFile() method.
     */
    public static void writeResultsIntoFile(Constituency constituency, CSVPrinter csvp){

        try{
            for (Candidate c:constituency.candidates){
                List candidateData = new ArrayList();
                candidateData.add(c.constituency);
                candidateData.add(c.name);
                candidateData.add(c.sex);
                candidateData.add(c.age);
                candidateData.add(c.category);
                candidateData.add(c.party);
                candidateData.add(c.general);
                candidateData.add(c.postal);
                candidateData.add(c.total);
                csvp.printRecord(candidateData);
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
    /*
    Method that takes a file as input and reads the details of each constituency and
    all the candidates under that constituency and writes all these details to a
    csv file.
     */
    public static void readResultsFromFile(File resultfile) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("res.csv");
            CSVPrinter csvp = null;
            CSVFormat csvf = CSVFormat.DEFAULT.withRecordSeparator(newLineSeparator);
            csvp = new CSVPrinter(fileWriter, csvf);
            csvp.printRecord(fileHeader);
            Constituency constituency = new Constituency();
            Candidate candidate = new Candidate();
            int flag = 0, constituencyFlag = 0, detailCounter = 0;
            FileReader fr = new FileReader(resultfile);
            LineNumberReader lnr = new LineNumberReader(fr);
            LineNumberReader nextReader;
            String line, nextLines;
            String candidateline = "(\\A)(\\d+)(\\s)(\\w*)";
            String constituencyline = "(\\A)(\\d+)([.])(\\s)(\\w+)";
            String sexline = "(\\A)([M F])";
            String ageline = "(\\A)^([0-9]){2}$";
            String categoryline = "(\\A)([SC GEN ST]{1,3}$)";
            String partyline = "(\\A)^(?!GEN|TURNOUT)([A-Z()]+){3,10}$";
            String generalline = "(\\A)^([0-9]){1,10}$";
            String postalline = "(\\A)^[0-9]{1,4}$";
            String totalline = "(\\A)^([0-9]{1,10}$)";
            Pattern candidatepattern = Pattern.compile(candidateline);
            Pattern constituencypattern = Pattern.compile(constituencyline);
            Pattern sexpattern = Pattern.compile(sexline);
            Pattern agepattern = Pattern.compile(ageline);
            Pattern categorypattern = Pattern.compile(categoryline);
            Pattern partypattern = Pattern.compile(partyline);
            Pattern generalpattern = Pattern.compile(generalline);
            Pattern postalpattern = Pattern.compile(postalline);
            Pattern totalpattern = Pattern.compile(totalline);
            while((line = lnr.readLine()) != null){
                if (line.toLowerCase().contains("detailed results")){
                    flag++;
                }
                if (flag >= 2){
                    Matcher candidatematcher = candidatepattern.matcher(line);
                    Matcher constituencymatcher = constituencypattern.matcher(line);
                    if (candidatematcher.find()) {
                        candidate.name = line;
                        candidate.constituency = constituency.name;
                        detailCounter = 1;
                        constituencyFlag = 1;
                        nextReader = lnr;
                        while(!(nextLines = nextReader.readLine()).isEmpty()){
                            nextLines.trim();
                            candidate.name = candidate.name.concat(" "+nextLines);
                        }
                        // System.out.println(candidate.name);
                    }
                    if (constituencymatcher.find()){
                        if(constituencyFlag == 1){
                            writeResultsIntoFile(constituency, csvp);
                            constituency = new Constituency();
                        }
                        constituency.name = line;
                        //System.out.println(constituency.name);
                        constituencyFlag = 0;
                    }
                    if (detailCounter > 0 && detailCounter <= 7){
                        if(!line.isEmpty()) {
                            switch (detailCounter) {
                                case 1:
                                    Matcher sexmatcher = sexpattern.matcher(line);
                                    if (sexmatcher.find()) {
                                        candidate.sex = line;
                                        detailCounter++;
                                    }
                                    break;
                                case 2:
                                    Matcher agematcher = agepattern.matcher(line);
                                    if (agematcher.find()) {
                                        candidate.age = line;
                                        detailCounter++;
                                    }
                                    break;
                                case 3:
                                    Matcher categorymatcher = categorypattern.matcher(line);
                                    if (categorymatcher.find()) {
                                        candidate.category = line;
                                        detailCounter++;
                                    }
                                    break;
                                case 4:
                                    Matcher partymatcher = partypattern.matcher(line);
                                    if (partymatcher.find()) {
                                        candidate.party = line;
                                        detailCounter++;
                                    }
                                    break;
                                case 5:
                                    Matcher generalmatcher = generalpattern.matcher(line);
                                    if (generalmatcher.find()) {
                                        candidate.general = line;
                                        detailCounter++;
                                    }
                                    break;
                                case 6:
                                    Matcher postalmatcher = postalpattern.matcher(line);
                                    if (postalmatcher.find()) {
                                        candidate.postal = line;
                                        detailCounter++;
                                    }
                                    break;
                                case 7:
                                    Matcher totalmatcher = totalpattern.matcher(line);
                                    if (totalmatcher.find()) {
                                        candidate.total = line;
                                        detailCounter++;
                                    }
                                default:
                                    break;
                            }
                        }
                    }
                    if (detailCounter > 7){
                        constituency.candidates.add(candidate);
                        candidate = new Candidate();
                        detailCounter = 0;
                    }
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }finally {
            try{
                fileWriter.flush();
                fileWriter.close();
            }catch(IOException e){
                System.out.println("Error while flushing/closing file");
            }
        }
    }
    public static void main(String[] args) throws IOException{
        File electionresults = new File("C:/Users/SHREYASH/Desktop/Stats_Report_AS2011.txt");
        readResultsFromFile(electionresults);
    }
}
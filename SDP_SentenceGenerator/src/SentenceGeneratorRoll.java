import java.util.*;
import java.util.concurrent.ThreadLocalRandom;



interface  SentenceGeneratorStrategy {
    public void generateSentence(ArrayList<String> vocabularyList);

}

class RandomSentenceGenerator implements SentenceGeneratorStrategy {

    //ArrayList<String> randomSentenceGeneratorVocabulary;

    String randomSentence;
    int index;

    @Override
    public void generateSentence(ArrayList<String> randomSentenceGeneratorVocabulary) {
        if (randomSentenceGeneratorVocabulary.size() == 0) {
            System.out.println("Sorry! no word is availale in vocabulary to make senetnce");
        } else {
            int size = randomSentenceGeneratorVocabulary.size();
            int minLen = 1;
            int maxLen = size;
            ThreadLocalRandom tlr = ThreadLocalRandom.current();
            int randomLen = tlr.nextInt(minLen, maxLen + 1);

            for (index = 1; index <= randomLen; index++) {
                int randomIndex = tlr.nextInt(0, maxLen);
                if (index == 1) {
                    randomSentence = randomSentenceGeneratorVocabulary.get(randomIndex);
                } else {
                    randomSentence = randomSentence + " " + randomSentenceGeneratorVocabulary.get(randomIndex);
                }
            }

            randomSentence = randomSentence + ".";

            System.out.println("Your generated sentence is following:");
            System.out.println(randomSentence);


        }
    }
}
class SortedSentenceGenerator implements SentenceGeneratorStrategy {
    String sentence, temp;
    int i;

    @Override
    public void generateSentence(ArrayList<String> sortedSentenceGeneratorVocabularyList) {

        System.out.println(sortedSentenceGeneratorVocabularyList.size());

        if (sortedSentenceGeneratorVocabularyList.size() == 0) {
            System.out.println("Sorry! no word is availale in vocabulary to make senetnce");
        } else {
            int size = sortedSentenceGeneratorVocabularyList.size();
            int minLen = 1;
            int maxLen = size;
            ThreadLocalRandom tlr = ThreadLocalRandom.current();
            int randomLen = tlr.nextInt(minLen, maxLen + 1);
            String words[] = new String[randomLen];

            for (i = 0; i < randomLen; i++) {
                int randomIndex = tlr.nextInt(0, maxLen);
                words[i] = sortedSentenceGeneratorVocabularyList.get(randomIndex);
            }

            for (int i = 0; i < randomLen; i++) {
                for (int j = i + 1; j < randomLen; j++) {
                    if (words[i].compareTo(words[j]) > 0) {
                        temp = words[i];
                        words[i] = words[j];
                        words[j] = temp;
                    }
                }
            }

            for (i = 0; i < randomLen; i++) {
                if (i == 0) {
                    sentence = words[i];
                } else {
                    sentence = sentence + " " + words[i];
                }
            }
            sentence = sentence + ".";

            System.out.println("Your generated sentence is following:");
            System.out.println(sentence);


        }
    }
}
class OrderedSentenceGenerator implements SentenceGeneratorStrategy{
    String sentence;
    int i;
    int count = 0;
    @Override
    public void generateSentence(ArrayList<String> orderedSentenceVocabularyList) {
        System.out.println(orderedSentenceVocabularyList.size());

        if(orderedSentenceVocabularyList.size() == 0){
            System.out.println("Sorry! no word is availale in vocabulary to make senetnce");
        }
        else{
            int size = orderedSentenceVocabularyList.size();
            int minLen = 1;
            int maxLen = size;
            ThreadLocalRandom tlr = ThreadLocalRandom.current();
            int randomLen = tlr.nextInt(minLen, maxLen + 1);

            Map<Integer,String> wordMap = new HashMap<>();

            for(i = 1; i<= randomLen; i++){
                int randomIndex = tlr.nextInt(0, maxLen);
                wordMap.put(randomIndex, orderedSentenceVocabularyList.get(randomIndex));
            }

            TreeMap<Integer, String> sorted = new TreeMap<>();

            // Copy all data from hashMap into TreeMap
            sorted.putAll(wordMap);

            // Display the TreeMap which is naturally sorted
            System.out.println("Your Generated sentence is following:");
            for (Map.Entry<Integer, String> entry : sorted.entrySet()){
                System.out.print(entry.getValue() + " ");
            }
        }

    }

}
interface  AddWordToInternalVocabulary{
    public  void addWordInVocabulary(ArrayList<String> sentenceVocbulary, String insertWord);
}

class  ToLowerCasWord implements AddWordToInternalVocabulary{

    @Override
    public void addWordInVocabulary(ArrayList<String> sentenceVocbulary, String insertWord) {
        int vocabularyListLength = sentenceVocbulary.size();
        String lowerCaseWord = insertWord.toLowerCase();
        //for(int  i = 0 ; i < sentenceLength;i++){
            //sentence[i]
        for(int i = 0 ; i< vocabularyListLength;i++){
            System.out.println(sentenceVocbulary.get(i));
        }
            sentenceVocbulary.add(insertWord);
            System.out.println(sentenceVocbulary.size());
           // System.out.println(sentenceVocbulary.);
        //}
    }
}

class SortedWordAdd implements AddWordToInternalVocabulary{

    @Override
    public void addWordInVocabulary(ArrayList<String> sentenceVocbulary, String insertWord) {

        int vocabularyListLength = sentenceVocbulary.size();
        for(int i = 0 ; i< vocabularyListLength;i++){
            System.out.println(sentenceVocbulary.get(i));
        }
        String lowerCaseWord = insertWord.toLowerCase();
        String lowerCaseSortedWord = lowerCaseWord.chars().sorted().collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append).toString();
        sentenceVocbulary.add(lowerCaseWord);
    }
}

class UpperCaseWord implements AddWordToInternalVocabulary{

    @Override
    public void addWordInVocabulary(ArrayList<String> sentenceVocbulary, String insertWord) {
        int vocabularyListLength = sentenceVocbulary.size();
        for(int i = 0 ; i< vocabularyListLength;i++){
            System.out.println(sentenceVocbulary.get(i));
        }
        String uppercaseWord = insertWord.toUpperCase();
        //String upperCaseReverseString = new StringBuilder(uppercaseWord).reverse().toString();
        sentenceVocbulary.add(uppercaseWord);

    }
}

class SentenceGenerator{
    ArrayList<String> vocabulary;
    SentenceGeneratorStrategy sentenceGeneratorStrategy;
    AddWordToInternalVocabulary addWordToInternalVocabulary;

    public SentenceGenerator(SentenceGeneratorStrategy sentenceGeneratorStrategy, AddWordToInternalVocabulary addWordToInternalVocabulary) {
        vocabulary = new ArrayList<>();
        this.sentenceGeneratorStrategy = sentenceGeneratorStrategy;
        this.addWordToInternalVocabulary = addWordToInternalVocabulary;
    }

    public void addWordToVocabularyList(String word){
        addWordToInternalVocabulary.addWordInVocabulary(vocabulary, word);
    }

    public void generate() {
        sentenceGeneratorStrategy.generateSentence(vocabulary);
    }
}



public class SentenceGeneratorRoll {
    public static void main(String[]args){
        SentenceGenerator randomSentenceGenerator = new SentenceGenerator(new RandomSentenceGenerator(), new ToLowerCasWord());
        SentenceGenerator sortedSentenceGenerator = new SentenceGenerator(new SortedSentenceGenerator(),new SortedWordAdd());
        SentenceGenerator orderedSentenceGenerator = new SentenceGenerator(new OrderedSentenceGenerator(), new UpperCaseWord() );
        boolean loop = true;
        while(loop){
            System.out.println("Enter Your Choice : ");
            System.out.println("1. Generate random sentence");
            System.out.println("2. Add new word to vocabulary");
            System.out.println("3. To Exit the console : ");
            Scanner choiceInput = new Scanner(System.in);
            int choice = choiceInput.nextInt();
            Scanner sc= new Scanner(System.in);


            if(choice == 1 || choice == 2) {
                System.out.println("Choose your vocabulary");
                System.out.println("1. Random Sentence Generator - RSG");
                System.out.println("2. Sorted Sentence Generator - SSG");
                System.out.println("3. Ordered Sentence Generator - OSG");
                int vocabularyChoice = choiceInput.nextInt();

                if(choice == 1 && vocabularyChoice == 1){
                    randomSentenceGenerator.generate();

                }
                else if(choice == 2 && vocabularyChoice == 1){
                    String word = sc.next();
                    randomSentenceGenerator.addWordToVocabularyList(word);

                }
                if(choice == 1 && vocabularyChoice == 2){

                    sortedSentenceGenerator.generate();

                }
                else if(choice == 2 && vocabularyChoice == 2){
                    String word = sc.next();
                    sortedSentenceGenerator.addWordToVocabularyList(word);
                }
                if(choice == 1 && vocabularyChoice == 3){
                    orderedSentenceGenerator.generate();
                }
                else if(choice == 2 && vocabularyChoice == 3){
                    String word = sc.next();
                    orderedSentenceGenerator.addWordToVocabularyList(word);
                }


            }
            if(choice == 3){
                System.out.println("You Exit the console : ");
                loop = false;
                return;
            }


        }
    }
}

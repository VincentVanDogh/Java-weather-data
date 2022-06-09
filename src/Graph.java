import java.awt.*;

public class Graph {
    final private String fileName = "weather_data.csv";
    final private int SUN_H = 16;
    final private int PRECP_SUM = 25;

    private int yearStart;
    private int csvFileStart;
    private int csvFileEnd;
    private int[] sunHours;
    private int[] rainfall;

    public Graph(int yearStart, int yearEnd) {
        this.yearStart = yearStart;
        this.csvFileStart = (yearStart - 1955) * 12 + 7;
        this.csvFileEnd = (yearEnd - 1955) * 12 + 10;
        this.sunHours = new int[yearEnd - yearStart + 1];
        this.rainfall = new int[yearEnd - yearStart + 1];
    }

    public void visualize(){
        extractData(readFileData(fileName, csvFileStart, csvFileEnd), sunHours, SUN_H, 12);
        extractData(readFileData(fileName, csvFileStart, csvFileEnd), rainfall, PRECP_SUM, 12);
        drawChart(sunHours, rainfall);
    }

    private String[] readFileData(String fileName, int lineStart, int lineEnd) {
        In fileReader = new In(fileName);

        int counter = 0;
        String[] file = fileReader.readAllLines(), copyOfRange = new String[lineEnd];
        for (int i = lineStart - 1; i < lineEnd; i++) {
            copyOfRange[counter] = file[i];
            counter++;
        }
        String[] empty = new String[counter];
        for (int i = 0; i < counter; i++) {
            empty[i] = copyOfRange[i];
        }
        return empty;
    }

    private void extractData(String[] dataArray, int[] resultArray, int numColumn, int entriesPerYear) {
        int sommer = 4, counter = 0;
        String[][] dataArrayCopy = new String[dataArray.length][];
        String[] summerArray = new String[sommer * ((dataArray.length / entriesPerYear) + 1)];

        for (int i = 0; i < dataArrayCopy.length; i++) {
            dataArrayCopy[i] = dataArray[i].split(";");

            if(i % entriesPerYear < 4){
                summerArray[counter] = dataArrayCopy[i][numColumn];
                counter++;
            }
        }

        for (int i = 0; i < resultArray.length; i++) {
            for (int j = sommer * i; j < sommer + (sommer * i); j++) {
                resultArray[i] += Integer.parseInt(summerArray[j]);
            }
        }
    }

    private void drawChart(int[] sunHours, int[] rainAmount) {
        int width = 1260;
        int height = 600;
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(-height * 0.25, height * 0.75);

        int minSonnenstunden = sunHours[0], maxSonnenstunden = 0;

        for (int i = 0; i < sunHours.length; i++) {
            minSonnenstunden = Math.min(minSonnenstunden, sunHours[i]);
            maxSonnenstunden = Math.max(maxSonnenstunden, sunHours[i]);
        }

        int minNiederschlagsmengen = rainAmount[0], maxNiederschlagsmengen = 0;
        for (int i = 0; i < rainAmount.length; i++) {
            minNiederschlagsmengen = Math.min(minNiederschlagsmengen, rainAmount[i]);
            maxNiederschlagsmengen = Math.max(maxNiederschlagsmengen, rainAmount[i]);
        }

        //Balken
        StdDraw.setPenColor(Color.RED);
        StdDraw.setPenRadius(0.005);
        for (int i = 0; i < sunHours.length; i++) {
            StdDraw.rectangle(45 + i * 30, sunHours[i] / 6.0, 10, sunHours[i] / 6.0 );
        }
        StdDraw.setPenColor(Color.BLUE);
        for (int i = 0; i < rainAmount.length; i++) {
            StdDraw.rectangle(45 + i * 30, - rainAmount[i] / 6.0, 10, rainAmount[i] / 6.0 );
        }

        //Schwarze Linien
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.line(34, maxSonnenstunden / 3.0, width - 34, maxSonnenstunden / 3.0);
        StdDraw.line(34, minSonnenstunden / 3.0, width - 34, minSonnenstunden / 3.0);
        StdDraw.line(34, - maxNiederschlagsmengen/3.0, width - 34, -maxNiederschlagsmengen / 3.0);
        StdDraw.line(34, - minNiederschlagsmengen/3.0, width - 34, -minNiederschlagsmengen / 3.0);

        StdDraw.setFont(new Font("Times", Font.PLAIN, 10));
        StdDraw.text(17, maxSonnenstunden / 3.0, Integer.toString(maxSonnenstunden));
        StdDraw.text(width - 17, maxSonnenstunden / 3.0, Integer.toString(maxSonnenstunden));

        StdDraw.text(17, minSonnenstunden / 3.0, Integer.toString(minSonnenstunden));
        StdDraw.text(width - 17, minSonnenstunden / 3.0, Integer.toString(minSonnenstunden));

        StdDraw.text(17, -maxNiederschlagsmengen / 3.0, Integer.toString(maxNiederschlagsmengen));
        StdDraw.text(width - 17, -maxNiederschlagsmengen / 3.0, Integer.toString(maxNiederschlagsmengen));

        StdDraw.text(17, -minNiederschlagsmengen / 3.0, Integer.toString(minNiederschlagsmengen));
        StdDraw.text(width - 17, -minNiederschlagsmengen / 3.0, Integer.toString(minNiederschlagsmengen));

        StdDraw.text(17, 10, "[h]");
        StdDraw.text(17, -10, "[mm]");

        for (int i = 0; i < sunHours.length; i++) {
            StdDraw.text(45 + i * 30, 10 , String.valueOf(yearStart + i));
        }

        //Nulllinie
        StdDraw.setPenRadius(0.0055);
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.line(7,0,width-7, 0);

        StdDraw.enableDoubleBuffering();
        StdDraw.save("./src/weather-graph.jpg");
    }
}

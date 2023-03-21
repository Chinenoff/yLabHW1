package org.example.homework3.file.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sorter {

  private final static String OUTPUTFILE = "sortedfile.txt";
  private final static int MEMORY = 50;

  private final int runNumber;

  public Sorter() {
    this.runNumber = MEMORY * 1024 * 1024 / 37;
  }

  public File sortFile(File dataFile) throws IOException {
    File fileresult = new File(OUTPUTFILE);

    FileReader fileReader;
    BufferedReader bufReader;
    ArrayList<FileReader> fileReaders;

    FileWriter fileWriter;
    BufferedWriter bufWriter;
    ArrayList<BufferedReader> bufReaders;

    List<Record> lines = new ArrayList<>();
    Record sortedLine;
    String line;
    int n = 0, i, fileNumber = 0;

    fileReader = new FileReader(dataFile);
    bufReader = new BufferedReader(fileReader);
    while ((line = bufReader.readLine()) != null) {
      if ("".equals(line.trim())) {
        continue;
      }
      n++;
      lines.add(new Record(line));
      if (n == this.runNumber) {
        lines.sort(new SortRecord());

        fileWriter = new FileWriter(createTempFile(fileNumber++));
        bufWriter = new BufferedWriter(fileWriter);
        for (i = 0; i < lines.size(); i++) {
          bufWriter.append(lines.get(i).line).append("\n");
        }
        bufWriter.close();
        fileWriter.close();

        n = 0;
        lines.clear();
      }
    }

    bufReader.close();
    fileReader.close();

    if (n > 0) {
      lines.sort(new SortRecord());

      fileWriter = new FileWriter(createTempFile(fileNumber));
      bufWriter = new BufferedWriter(fileWriter);
      for (i = 0; i < lines.size(); i++) {
        bufWriter.append(lines.get(i).line).append("\n");
      }
      bufWriter.close();
      fileWriter.close();

      lines.clear();
    }

    fileReaders = new ArrayList<>();
    bufReaders = new ArrayList<>();
    for (i = 0; i <= fileNumber; i++) {
      fileReaders.add(new FileReader("tmp" + i));
      bufReaders.add(new BufferedReader(fileReaders.get(i)));
    }

    fileWriter = new FileWriter(fileresult);
    bufWriter = new BufferedWriter(fileWriter);

    for (i = 0; i < bufReaders.size(); i++) {
      if ((line = bufReaders.get(i).readLine()) != null) {
        lines.add(new Record(line, i));
      }
    }
    while (lines.size() != 0) {
      lines.sort(new SortRecord());

      sortedLine = lines.get(0);
      lines.remove(0);
      bufWriter.append(sortedLine.line).append("\n");
      n = sortedLine.getFileIndex();

      if ((line = bufReaders.get(n).readLine()) != null) {
        lines.add(new Record(line, n));
      }
    }

    bufWriter.close();
    fileWriter.close();

    for (i = 0; i < bufReaders.size(); i++) {
      bufReaders.get(i).close();
      fileReaders.get(i).close();
    }
    return fileresult;
  }

  private File createTempFile(int index) {
    File file = new File("tmp" + index);
    file.deleteOnExit();
    return file;
  }


  static class Record {

    private final String line;
    private int fileIndex;

    public Record(String line) {
      this.line = line;
    }

    public Record(String line, int fileIndex) {
      this.line = line;
      this.fileIndex = fileIndex;
    }

    public int getFileIndex() {
      return this.fileIndex;
    }
  }

  static class SortRecord implements Comparator<Record> {

    @Override
    public int compare(Record record1, Record record2) {
      Long w1 = Long.parseLong(record1.line);
      Long w2 = Long.parseLong(record2.line);
      return w1.compareTo(w2);
    }
  }
}


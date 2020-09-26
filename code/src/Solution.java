import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * @author wangz
 */
public class Solution {
    public static void main(String[] args) throws IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();
        writer.connect(reader);

        Thread printThread = new Thread(new Print(reader), "PrintThread");
        Thread inputThread = new Thread(new Reader(writer), "InputThread");
        inputThread.start();
        printThread.start();


    }
}

class Print implements Runnable {
    PipedReader in;

    public Print(PipedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        int rec;
        while (true) {
            try {
                if (((rec = in.read()) == -1)) {
                    break;
                }
                System.out.print((char) rec);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


class Reader implements Runnable {
    PipedWriter writer;

    public Reader(PipedWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {

        int rec;

        while (true) {
            try {
                if (((rec = System.in.read()) == -1)) {
                    break;
                }
                writer.write(rec);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

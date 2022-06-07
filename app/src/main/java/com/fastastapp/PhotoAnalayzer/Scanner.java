package com.fastastapp.PhotoAnalayzer;

import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.*;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.*;



import static org.opencv.imgproc.Imgproc.drawContours;

import android.graphics.Bitmap;

public class Scanner {
    private final Mat source;
    private final int questionCount;

    private Rect roi;
    private Mat  gray, hierarchy,cut,cutGray;
    private List<MatOfPoint> contours, bubbles, sortedBubbles;
    private List<Integer> answers;
    private final QRCodeDetector qrCodeDetector;
    Bitmap bitmap;
    List<Answers> answer_key = Arrays.asList(
            Answers.A, Answers.A, Answers.B, Answers.C, Answers.D,
            Answers.D, Answers.A, Answers.A, Answers.B, Answers.B,
            Answers.A, Answers.D, Answers.C, Answers.A, Answers.D,
            Answers.C, Answers.C, Answers.D, Answers.A, Answers.A);


    private boolean logging = false;

    public Scanner(Mat source, int questionCount) {
        this.source = source;
        this.questionCount = questionCount;

        this.qrCodeDetector = new QRCodeDetector();
bitmap =null;

        hierarchy = new Mat();
        contours = new ArrayList<>();
        bubbles = new ArrayList<>();
        sortedBubbles = new ArrayList<>();
        answers = new ArrayList<>();
    }
    public String detectQRCode( ){
        return qrCodeDetector.detectAndDecode(source);
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public  void  filter(){
        gray = new Mat(source.size(), CV_8UC1);
        cvtColor(source, gray, COLOR_RGB2GRAY);
    }
    public Bitmap scan() throws Exception {



        //CV_8UC1 It is an 8-bit single-channel array that is primarily used to store and obtain the values of any image
        gray = new Mat(source.size(), CV_8UC1);
        cvtColor(source, gray, COLOR_RGB2GRAY);
        if(logging) Util.writeToFile(gray, "step_1_gray.png");

        GaussianBlur(gray, gray, new Size(5, 5), 0);
        if(logging) Util.writeToFile(gray, "step_2_blur.png");

        Canny(gray, gray, 75, 200);
        if(logging) Util.writeToFile(gray, "step_3_canny.png");

        cut =  findParentRectangle();

        cutGray = new Mat(source.size(), CV_8UC1);
        cvtColor(cut, cutGray, COLOR_RGB2GRAY);
        if(logging) Util.writeToFile(cutGray, "step_4_cutGray.png");


        GaussianBlur(cutGray, cutGray, new Size(5, 5), 0);
        if(logging) Util.writeToFile(cutGray, "step_5_cutBlur.png");


        threshold(cutGray,cutGray,0,255, THRESH_BINARY_INV|THRESH_OTSU);
        if(logging) Util.writeToFile(cutGray, "step_6_thresh.png");



        findBubbles();

        recognizeAnswers();
        return bitmap;


    }
    private Mat findParentRectangle() throws Exception {

        findContours(gray.clone(), contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);

        // find rectangles
        HashMap<Double, MatOfPoint> rectangles = new HashMap<>();
        for(int i = 0; i < contours.size(); i++){
            MatOfPoint2f approxCurve = new MatOfPoint2f( contours.get(i).toArray() );
            approxPolyDP(approxCurve, approxCurve, 0.02 * arcLength(approxCurve, true), true);

            if(approxCurve.toArray().length == 4){
                rectangles.put((double) i, contours.get(i));
            }
        }



        int parentIndex = -1;

        // choose hierarchical rectangle which is our main wrapper rect
        for (Map.Entry<Double, MatOfPoint> rectangle : rectangles.entrySet()) {
            double index = rectangle.getKey();

            double[] ids = hierarchy.get(0, (int) index);
            double nextId = ids[0];
            double previousId = ids[1];

            if(nextId != -1 && previousId != -1) continue;

            int k = (int) index;
            int c = 0;

            while(hierarchy.get(0, k)[2] != -1){
                k = (int) hierarchy.get(0, k)[2];
                c++;
            }

            if(hierarchy.get(0, k)[2] != -1) c++;

            if (c >= 3){
                parentIndex = (int) index;
            }

        }




        if(parentIndex < 0){
            throw new Exception("Couldn't capture main wrapper");
        }

        roi = boundingRect(contours.get(parentIndex));



        int padding = 30;

        roi.x += padding;
        roi.y += padding;
        roi.width -= 2 * padding;
        roi.height -= 2 * padding;


        if(logging) Util.writeToFile(source.submat(roi), "step_7_roi.png");

        return source.submat(roi);
    }

    private void findBubbles() throws Exception {

        contours.clear();

        findContours(cutGray.clone(), contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        Rect tmp;
        hierarchy.release();

        Mat cutCont;
        cutCont = cut.clone();


        //Determining the contours of the bubble
        for(int c = 0; c < contours.size(); c++)
        {
            tmp = Imgproc.boundingRect(contours.get(c));
            double ar =tmp.width/(float)tmp.height;

            if( tmp.width >= 20 && tmp.height>=20 && ar >= 0.9 && ar<=1.2 ){
                bubbles.add(contours.get(c));
                Scalar color = new Scalar(0, 0, 255);
                Imgproc.drawContours(cutCont, contours, c, color, 2,
                        Imgproc.LINE_8, hierarchy, 2, new Point() ) ;
            }
        }

        if(bubbles.size() != 20 * 4){
            throw new Exception("Couldn't capture all bubbles.");
        }



        // order bubbles on coordinate system

        Util.sortTopLeft2BottomRight(bubbles);


        for(int j = 0; j < bubbles.size(); j+=4*2){

            List<MatOfPoint> row = bubbles.subList(j, j + 4*2);

            Util.sortLeft2Right(row);

            sortedBubbles.addAll(row);
        }
    }

    private void recognizeAnswers(){
        List<Integer> tmp1 = new ArrayList<>();

        List<Answers> helpAns = new ArrayList<>();
        for(int i =0;i< answer_key.size()/2;i++){
            helpAns.add(answer_key.get(i));
            helpAns.add(answer_key.get(answer_key.size()/2+i));

        }

        for(int i = 0; i< sortedBubbles.size(); i+=4) {
            int question =((i+1)/4+1);

            List<MatOfPoint> rows = sortedBubbles.subList(i, i+4);
            for (int j = 0; j < rows.size(); j++) {

                MatOfPoint col = rows.get(j);
                List<MatOfPoint> list = Collections.singletonList(col);
                Mat mask = new Mat(cutGray.size(), CvType.CV_8UC1);

                drawContours(mask, list, -1, new Scalar(1, 1, 1), -1);
                Mat conjunction = new Mat(cutGray.size(), CvType.CV_8UC1);
                Core.bitwise_and(cutGray, mask, conjunction);


                int countNonZero = Core.countNonZero(conjunction);
                System.out.println(countNonZero);

                Answers letter = Answers.valueOf(j+1);

                if(i==0 &&j==0) countNonZero-=9500;
                if(countNonZero >600 ){
                    tmp1.add(letter.ordinal());
                    if(helpAns.get(question-1).equals(letter)){
                        Imgproc.drawContours(cut, sortedBubbles, i + j, new Scalar(0, 255, 0), 2,
                                Imgproc.LINE_8, hierarchy, 2, new Point());
                    }else {
                        Imgproc.drawContours(cut, sortedBubbles, i +helpAns.get(question-1).ordinal()-1 , new Scalar(255, 0, 255), 2,
                                Imgproc.LINE_8, hierarchy, 2, new Point());
                    }
                    break;

                }

            }

            if(tmp1.size() != question) {
                tmp1.add(Answers.EMPTY.ordinal());
                Imgproc.drawContours(cut, sortedBubbles, i +helpAns.get(question-1).ordinal()-1 , new Scalar(0, 0, 255), 2,
                        Imgproc.LINE_8, hierarchy, 2, new Point());

            }

            }

        List<Integer> odds = new ArrayList<>();
        List<Integer> evens = new ArrayList<>();
        for(int i = 0; i < answers.size(); i++){
            if(i % 2 == 0) odds.add(answers.get(i));
            if(i % 2 == 1) evens.add(answers.get(i));
        }

        answers.clear();
        answers.addAll(odds);
        answers.addAll(evens);
        bitmap = Bitmap.createBitmap(cut.cols(), cut.rows(), Bitmap.Config.ARGB_8888);
      //  Util.writeToFile(cut, "result.jpg");
        Utils.matToBitmap(cut,bitmap);
    }




}

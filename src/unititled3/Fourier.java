package unititled3;


import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

public final class Fourier {

    public static final int N = 32;
    public static final int M = 32;

    private static ArrayList<Double> phases = new ArrayList(N);

    public static ArrayList<Complex> DFT (ArrayList digitalValues) {

        ArrayList<Complex> cx = new ArrayList(M);

        for (int m = 0; m < M; m++) {

            Complex cm = new Complex(0, 0);
            double t = (2 * Math.PI * m) / N;

            for (int n = 0; n < N; n++) {

                double value = (double)digitalValues.get(n);

                double r = Math.cos(t*n);
                double im = Math.sin(t*n);

                Complex c = new Complex(r, -im).multiply(value);
                cm = cm.add(c);
            }

            phases.add(Math.atan(cm.getImaginary() / cm.getReal()));
            cx.add(cm);
        }

        return cx;
    }

    public static ArrayList<Double> RDFT (ArrayList<Complex> cx) {

        ArrayList<Double> digitalValues = new ArrayList(N);

        ArrayList<Double> amplitudes = new ArrayList<>();
        for (int i = 0; i < cx.size(); i++) {
            Complex ci = cx.get(i);
            amplitudes.add(ci.abs());
        }

        for (int n = 0; n < N; n++) {

            Complex xn = new Complex(0,0);

            for (int m = 0; m < N; m++) {

                double value = amplitudes.get(m);
                double r = value * Math.cos((2 * Math.PI * n * m) / N - phases.get(m));
                double im = value * Math.sin((2 * Math.PI * n * m) / N - phases.get(m));

                Complex c = new Complex(r, im);
                xn = xn.add(c);
            }
            xn = xn.divide(N);
            digitalValues.add(xn.getReal());
        }

        return digitalValues;
    }

    public static ArrayList<Complex> FFT (ArrayList digitalValues)
    {
        ArrayList<Complex> complexValues = new ArrayList();

        for (int i = 0; i < digitalValues.size(); i++) {
            double value = (double) digitalValues.get(i);
            Complex ci = new Complex(value, 0);
            complexValues.add(ci);
        }
        FFT1(complexValues,1);

        return complexValues;
    }

    public static ArrayList<Complex> FFT1 (ArrayList<Complex> complexValues,int dir) {
        int N = complexValues.size();
        if (N == 1) return complexValues;
        System.out.println(N);
        ArrayList first = new ArrayList(N/2+N%2);
        ArrayList second = new ArrayList(N/2);

        for (int i = 0; i <complexValues.size(); i++)
        {
            if (i % 2 == 0) first.add(complexValues.get(i));
            else second.add(complexValues.get(i));
        }

        ArrayList<Complex> b1 = FFT1(first,dir);
        ArrayList<Complex> b2 = FFT1(second,dir);

        Complex wN = new Complex(Math.cos(2 * Math.PI / N), dir * Math.sin(2 * Math.PI / N));
        Complex w = new Complex(1, 0);
        ArrayList y = new ArrayList(N);

        for (int j = 0; j < N / 2; j++)
        {
            y.add(j,b1.get(j).add(b2.get(j).multiply(w)));
            y.add(j+N/2,b1.get(j).subtract(b2.get(j).multiply(w)));
            w = w.subtract(wN);
            //count++;
        }

        return y;
    }




    public static ArrayList<Double> RFFT (ArrayList<Complex> cx) {
        ArrayList<Double> digitalValues = new ArrayList();
/**
       ArrayList<Complex> complexNumbers = butterfly(cx, N, -1);


        for (int i = 0; i < complexNumbers.size(); i++) {
            Complex c = complexNumbers.get(i);
            digitalValues.add(c.getReal() / N);
        }return digitalValues;
        **/
        Complex temp;
        int Len = cx.size();
        if (Len <= 2) return digitalValues;
        int r = 0;
        for (int x = 1; x < Len; x++)
        {
            r = rev_next(r, Len);
            if (r > x)
            {
                temp = cx.get(x);
                cx.set(x,cx.get(r));
                cx.set(r,temp);
            }
        }

        for (int x = 0; x < Len; x++)
        {
            digitalValues.add(x,cx.get(x).getReal());
        }
        return digitalValues;
    }

    static private int rev_next(int r, int n)
    {
        // преобразовывает r=rev(x-1) в rev(x)
        do
        {
            n = n >> 1; r = r ^ n;
        } while ((r & n) == 0);
        return r;
    }


















    private static ArrayList<Complex> butterfly(ArrayList<Complex> c, int count, int dir) {

        if (count == 1) {
            return c;
        }

        ArrayList<Complex> first = new ArrayList();
        ArrayList<Complex> second = new ArrayList();

        for (int i = 0; i < count / 2; i++)
        {
            Complex c1 = c.get(i);
            Complex c2 = c.get(i + count / 2);

            double r = Math.cos(2 * Math.PI * i / count);
            double im = Math.sin(2 * Math.PI * dir * i / count);
            Complex c3 = new Complex(r, im);

            first.add(c1.add(c2));
            second.add(c1.subtract(c2).multiply(c3));
        }

        ArrayList<Complex> res1 = butterfly(first, count/2, dir);
        ArrayList<Complex> res2 = butterfly(second, count/2, dir);

        ArrayList<Complex> res3 = new ArrayList<>();
        for (int i = 0; i < count / 2 ; i++)
        {
            res3.add(res1.get(i));
            res3.add(res2.get(i));
        }

        return res3;
    }
}

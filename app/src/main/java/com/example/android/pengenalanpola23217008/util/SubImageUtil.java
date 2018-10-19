package com.example.android.pengenalanpola23217008.util;

import com.example.android.pengenalanpola23217008.model.SkeletonFeature;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SubImageUtil {

    // -----------------------------
    // Zhang Suen Thinning Algorithm
    // -----------------------------

    public static int zhangSuenStep(int[] pixels, int xmin, int ymin, int xmax, int ymax, int width) {
        int count = 0;

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                if ((pixels[i + j * width] & 0x000000ff) == 0) {
                    int[] neighbours = {
                            pixels[i + (j - 1) * width] & 0x000000ff,
                            pixels[(i + 1) + (j - 1) * width] & 0x000000ff,
                            pixels[(i + 1) + j * width] & 0x000000ff,
                            pixels[(i + 1) + (j + 1) * width] & 0x000000ff,
                            pixels[i + (j + 1) * width] & 0x000000ff,
                            pixels[(i - 1) + (j + 1) * width] & 0x000000ff,
                            pixels[(i - 1) + j * width] & 0x000000ff,
                            pixels[(i - 1) + (j - 1) * width] & 0x000000ff
                    };
                    int[] function = zhangSuenAB(neighbours);

                    if (function[1] < 2 || 6 < function[1]) continue;
                    if (function[0] != 1) continue;
                    if (neighbours[0] != 255 && neighbours[2] != 255 && neighbours[4] != 255)
                        continue;
                    if (neighbours[2] != 255 && neighbours[4] != 255 && neighbours[6] != 255)
                        continue;

                    pixels[i + j * width] = pixels[i + j * width] | 0x0000ff00;
                }
            }
        }

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                if ((pixels[i + j * width] & 0x00ffffff) == 0x0000ff00) {
                    pixels[i + j * width] = pixels[i + j * width] | 0x00ffffff;
                    count++;
                }
            }
        }

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                if ((pixels[i + j * width] & 0x000000ff) == 0) {
                    int[] neighbours = {
                            pixels[i + (j - 1) * width] & 0x000000ff,
                            pixels[(i + 1) + (j - 1) * width] & 0x000000ff,
                            pixels[(i + 1) + j * width] & 0x000000ff,
                            pixels[(i + 1) + (j + 1) * width] & 0x000000ff,
                            pixels[i + (j + 1) * width] & 0x000000ff,
                            pixels[(i - 1) + (j + 1) * width] & 0x000000ff,
                            pixels[(i - 1) + j * width] & 0x000000ff,
                            pixels[(i - 1) + (j - 1) * width] & 0x000000ff
                    };
                    int[] function = zhangSuenAB(neighbours);

                    if (function[1] < 2 || 6 < function[1]) continue;
                    if (function[0] != 1) continue;
                    if (neighbours[0] != 255 && neighbours[2] != 255 && neighbours[6] != 255)
                        continue;
                    if (neighbours[0] != 255 && neighbours[4] != 255 && neighbours[6] != 255)
                        continue;

                    pixels[i + j * width] = pixels[i + j * width] | 0x0000ff00;
                }
            }
        }

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                if ((pixels[i + j * width] & 0x00ffffff) == 0x0000ff00) {
                    pixels[i + j * width] = pixels[i + j * width] | 0x00ffffff;
                    count++;
                }
            }
        }

        return count;
    }

    private static int[] zhangSuenAB(int[] neighbours) {
        int countA = 0;
        int countB = 0;

        for (int i = 0; i < 8; i++) {
            if (neighbours[i] == 255 && neighbours[(i + 1) % 8] == 0) {
                countA++;
            }
            if (neighbours[i] == 0) {
                countB++;
            }
        }

        return new int[]{countA, countB};
    }

    // -----------------------------
    // Custom Thinning Algorithm
    // -----------------------------

    public static void customStep(int[] pixels, int xmin, int ymin, int xmax, int ymax, int x, int y, int width) {
        int counterDirection, length, c, d, averageLength;

        int a = x;
        int b = y;
        int direction = 2;
        int totalLength = 0;
        int chainCount = 0;
        int[][] neighbours = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

        do {
            int i = 0;
            for (; i < 8; i++) {
                int pixel = pixels[(a + neighbours[(direction + i + 5) % 8][0]) + (b + neighbours[(direction + i + 5) % 8][1]) * width] & 0x000000ff;
                if (pixel == 0) break;
            }

            if (i == 9) {
                return;
            } else {
                direction = (direction + i + 5) % 8;
                counterDirection = (direction + 2) % 8;
                c = a + neighbours[counterDirection][0];
                d = b + neighbours[counterDirection][1];
                length = 0;

                while ((pixels[c + d * width] & 0x000000ff) == 0) {
                    c = c + neighbours[counterDirection][0];
                    d = d + neighbours[counterDirection][1];
                    length++;
                }

                if (length > 1) {
                    totalLength += length;
                    chainCount++;
                }

                a = a + neighbours[direction][0];
                b = b + neighbours[direction][1];
            }
        }
        while (!(a == x && b == y));

        averageLength = totalLength / chainCount;
        a = x;
        b = y;
        direction = 2;

        do {
            int i = 0;
            for (; i < 8; i++) {
                int pixel = pixels[(a + neighbours[(direction + i + 5) % 8][0]) + (b + neighbours[(direction + i + 5) % 8][1]) * width] & 0x000000ff;
                if (pixel == 0) break;
            }

            if (i == 9) {
                return;
            } else {
                direction = (direction + i + 5) % 8;
                counterDirection = (direction + 2) % 8;
                c = a + neighbours[counterDirection][0];
                d = b + neighbours[counterDirection][1];
                length = 0;

                while ((pixels[c + d * width] & 0x000000ff) == 0) {
                    c = c + neighbours[counterDirection][0];
                    d = d + neighbours[counterDirection][1];
                    length++;
                }

                if (length > 1 && length <= averageLength) {
                    c = (c - 1 + a) / 2;
                    d = (d - 1 + b) / 2;
                    pixels[c + d * width] = pixels[c + d * width] | 0x0000ff00;
                }

                a = a + neighbours[direction][0];
                b = b + neighbours[direction][1];
            }
        }
        while (!(a == x && b == y));

        for (b = ymin; b <= ymax; b++) {
            for (a = xmin; a <= xmax; a++) {
                if ((pixels[a + b * width] & 0x00ffffff) == 0x0000ff00) {
                    pixels[a + b * width] = pixels[a + b * width] & 0xff000000;
                } else if ((pixels[a + b * width] & 0x000000ff) == 0) {
                    pixels[a + b * width] = pixels[a + b * width] | 0x00ffffff;
                }
            }
        }
    }

    // -----------------------------
    // Flood Fill Algorithm
    // -----------------------------

    public static int[] floodFill(int[] pixels, int x, int y, int width) {

        int xmax = x;
        int xmin = x;
        int ymax = y;
        int ymin = y;
        Queue<Integer> queueX = new LinkedList<>();
        Queue<Integer> queueY = new LinkedList<>();

        queueX.offer(x);
        queueY.offer(y);

        while (!queueX.isEmpty()) {
            x = queueX.poll();
            y = queueY.poll();

            int pixel = pixels[x + y * width] & 0x000000ff;

            if (pixel != 255) {
                pixels[x + y * width] = pixels[x + y * width] | 0x00ffffff;

                if (x < xmin) xmin = x;
                if (x > xmax) xmax = x;
                if (y < ymin) ymin = y;
                if (y > ymax) ymax = y;

                queueX.offer(x);
                queueY.offer(y + 1);
                queueX.offer(x);
                queueY.offer(y - 1);
                queueX.offer(x + 1);
                queueY.offer(y);
                queueX.offer(x - 1);
                queueY.offer(y);
            }
        }

        return new int[]{xmin, ymin, xmax, ymax};
    }

    public static int[] getNewBorder(int[] pixels, int xmin, int ymin, int xmax, int ymax, int width) {

        int pxmin = (xmax + xmin) / 2;
        int pxmax = (xmax + xmin) / 2;
        int pymin = (ymax + ymin) / 2;
        int pymax = (ymax + ymin) / 2;

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                int p = i + j * width;

                if ((pixels[p] & 0x00ffffff) == 0x00000000) {
                    if (p % width < pxmin) pxmin = p % width;
                    if (p % width > pxmax) pxmax = p % width;
                    if (p / width < pymin) pymin = p / width;
                    if (p / width > pymax) pymax = p / width;
                }
            }
        }

        return new int[]{pxmin, pymin, pxmax, pymax};
    }

    // -----------------------------
    // Feature Extraction Algorithm
    // -----------------------------

    public static SkeletonFeature extractFeature(int[] pixels, int xmin, int ymin, int xmax, int ymax, int width) {

        SkeletonFeature sf = new SkeletonFeature();

        // titik ujung
        List<Integer> endpoints = new ArrayList<>();

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                int p = i + j * width;

                if ((pixels[p] & 0x00ffffff) != 0x00ffffff) {
                    int[] neighbour = {
                            p - width,
                            p - width + 1,
                            p + 1,
                            p + width + 1,
                            p + width,
                            p + width - 1,
                            p - 1,
                            p - width - 1
                    };
                    int black = 0;
                    int index = -1;

                    for (int k = 0; k < neighbour.length; k++) {
                        if ((pixels[neighbour[k]] & 0x00ffffff) != 0x00ffffff) {
                            black++;
                            index = k;
                        }
                    }

                    if (black == 1) {
                        endpoints.add((index + 4) % 8);
                    }
                }
            }
        }

        sf.endpoints = endpoints;

        // garis tegak
        int[] h = new int[ymax - ymin + 1];
        int[] v = new int[xmax - xmin + 1];

        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                int p = i + j * width;

                if ((pixels[p] & 0x00ffffff) != 0x00ffffff) {
                    h[j - ymin]++;
                    v[i - xmin]++;
                }
            }
        }

        int[] hsum = new int[3];
        for (int i = 0; i < h.length; i++) {
            if (h[i] > (xmax - xmin + 1) / 2 && h[i] > 1) {
                if (i < (ymax - ymin) * 4 / 10) {
                    hsum[0]++;
                } else if (i < (ymax - ymin) * 6 / 10) {
                    hsum[1]++;
                } else {
                    hsum[2]++;
                }
            }
        }

        int[] vsum = new int[3];
        for (int i = 0; i < v.length; i++) {
            if (v[i] > (ymax - ymin + 1) / 2 && v[i] > 0) {
                if (i < (xmax - xmin) * 4 / 10) {
                    vsum[0]++;
                } else if (i < (xmax - xmin) * 6 / 10) {
                    vsum[1]++;
                } else {
                    vsum[2]++;
                }
            }
        }

        sf.hTop = hsum[0] > 0;
        sf.hMid = hsum[1] > 0;
        sf.hBottom = hsum[2] > 0;
        sf.vLeft = vsum[0] > 0;
        sf.vMid = vsum[1] > 0;
        sf.vRight = vsum[2] > 0;

        // lubang
        int[] hole = new int[3];
        for (int j = ymin; j <= ymax; j++) {
            for (int i = xmin; i <= xmax; i++) {
                int p = i + j * width;

                if ((pixels[p] & 0x00ffffff) == 0x00ffffff) {
                    int midpoint = holeFloodFill(pixels, xmin - 1, ymin - 1, xmax + 1, ymax + 1, width, p);

                    if (midpoint / width < (ymax - ymin + 2) * 4 / 10 + (ymin - 1)) {
                        hole[0]++;
                    } else if (midpoint / width < (ymax - ymin + 2) * 6 / 10 + (ymin - 1)) {
                        hole[1]++;
                    } else {
                        hole[2]++;
                    }
                }
            }
        }

        sf.lTop = hole[0] > 0;
        sf.lMid = hole[1] - 1 > 0;
        sf.lBottom = hole[2] > 0;

        return sf;
    }

    private static int holeFloodFill(int[] pixels, int xmin, int ymin, int xmax, int ymax, int width, int p) {

        int pxmin = p % width;
        int pxmax = p % width;
        int pymin = p / width;
        int pymax = p / width;
        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(p);

        while (!queue.isEmpty()) {
            int pt = queue.poll();

            if ((pixels[pt] & 0x00ffffff) == 0x00ffffff
                    && xmin <= (pt % width)
                    && (pt % width) <= xmax
                    && ymin <= (pt / width)
                    && (pt / width) <= ymax) {
                pixels[pt] = (pixels[pt] & 0xff000000);

                if (pt % width < pxmin) pxmin = pt % width;
                if (pt % width > pxmax) pxmax = pt % width;
                if (pt / width < pymin) pymin = pt / width;
                if (pt / width > pymax) pymax = pt / width;

                queue.offer(pt - width);
                queue.offer(pt + 1);
                queue.offer(pt + width);
                queue.offer(pt - 1);
            }
        }

        return (pxmax + pxmin) / 2 + (pymax + pymin) / 2 * width;
    }
}

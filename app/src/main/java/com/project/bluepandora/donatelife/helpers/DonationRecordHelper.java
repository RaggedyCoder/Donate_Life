package com.project.bluepandora.donatelife.helpers;

import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.project.bluepandora.donatelife.data.DRItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import nineoldandroids.view.ViewHelper;

@SuppressWarnings("ALL")
public class DonationRecordHelper {
    private static ArrayList<DRItem> itemSort;
    private static ArrayList<DRItem> divider;
    private static RectF mRect1 = new RectF();
    private static RectF mRect2 = new RectF();
    private static SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static void sort(ArrayList<DRItem> items) {
        itemSort = items;
        divider = new ArrayList<DRItem>(items.size());
        for (int i = 0; i < items.size(); i++) {
            divider.add(new DRItem());
        }
        mergeSort(0, items.size() - 1);
    }

    private static void mergeSort(int lowerIndex, int higherIndex) {
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the sortArray
            mergeSort(lowerIndex, middle);
            // Below step sorts the right side of the sortArray
            mergeSort(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }

    private static void mergeParts(int lowerIndex, int middle, int higherIndex) {
        for (int i = lowerIndex; i <= higherIndex; i++) {
            divider.set(i, itemSort.get(i));
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        Date iDate = null;
        Date jDate = null;
        while (i <= middle && j <= higherIndex) {
            try {
                try {
                    iDate = isoFormat.parse(divider.get(i).getDonationTime().replace(".0", ""));
                } catch (Exception e) {
                    Log.e("iDate", e.getMessage() + " " + divider.get(i).getDonationTime().replace(".0", ""));
                }
                try {
                    jDate = isoFormat.parse(divider.get(j).getDonationTime());
                } catch (Exception e) {
                    Log.e("jDate", e.getMessage() + " " + divider.get(j).getDonationTime());
                }
                if (iDate.getTime() <= jDate.getTime()) {
                    itemSort.set(k, divider.get(i));
                    i++;
                } else {
                    itemSort.set(k, divider.get(j));
                    j++;
                }
                k++;
            } catch (Exception e) {
                Log.e("NullPointerException", "" + divider.size() + " " + itemSort.size());
            }
        }
        while (i <= middle) {
            itemSort.set(k, divider.get(i));
            k++;
            i++;
        }
    }

    public static int getScrollY(AbsListView view, int mHeaderHeight) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static boolean search(ArrayList<DRItem> items, String searchKey) {
        items.remove(0);
        items.remove(0);
        items.remove(items.size() - 1);
        Log.e("size", items.size() + "");
        if (items.size() == 0) {
            items.add(new DRItem("0", "0"));
            items.add(new DRItem("0", "0"));
            items.add(new DRItem("z", "z"));
            Log.e("size1", items.size() + "");
            return false;
        }
        isoFormat.setTimeZone(TimeZone.getDefault());
        Date searchDate = null;
        try {
            searchDate = isoFormat.parse(searchKey);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        int lowArray = 0, highArray = items.size() - 1, midArray;
        int sign = 1;
        Date checkDate = null;
        while (lowArray <= highArray) {
            midArray = (lowArray + highArray) / 2;
            isoFormat.setTimeZone(TimeZone.getDefault());
            Date donatedDate = null;
            try {
                donatedDate = isoFormat.parse(items.get(midArray).getDonationTime());
            } catch (ParseException e) {

                e.printStackTrace();
            }
            checkDate = new Date(Math.abs(searchDate.getTime() - donatedDate.getTime()));
            if ((donatedDate.getTime() > searchDate.getTime())) {
                sign = 1;
            } else if ((donatedDate.getTime() < searchDate.getTime())) {
                sign = -1;
            } else {
                items.add(0, new DRItem("0", "0"));
                items.add(0, new DRItem("0", "0"));
                items.add(new DRItem("z", "z"));
                Log.e("size2", items.size() + "");
                return true;
            }
            Log.e("TIME", checkDate.getYear() + " " + searchKey + " " + items.get(midArray).getDonationTime() + " " + isoFormat.format(checkDate) + " " + (searchDate.getTime() - donatedDate.getTime()));
            if (checkDate.getYear() == 70) {
                if (checkDate.getMonth() <= 3) {
                    items.add(0, new DRItem("0", "0"));
                    items.add(0, new DRItem("0", "0"));
                    items.add(new DRItem("z", "z"));
                    Log.e("size3", items.size() + "");
                    return true;
                } else {
                    if (checkDate.getMonth() * sign > 3) {
                        highArray = midArray - 1;
                    } else if (checkDate.getMonth() * sign < -3) {
                        lowArray = midArray + 1;
                    }
                }
            } else {
                if (sign == 1) {
                    highArray = midArray - 1;
                } else if (sign == -1) {
                    lowArray = midArray + 1;
                }
            }
        }
        items.add(0, new DRItem("0", "0"));
        items.add(0, new DRItem("0", "0"));
        items.add(new DRItem("z", "z"));
        Log.e("size4", items.size() + "");
        return false;

    }

    public static void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);
        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));
        ViewHelper.setTranslationX(view1, translationX);
        ViewHelper.setTranslationY(view1, translationY);
        ViewHelper.setScaleX(view1, scaleX);
        ViewHelper.setScaleY(view1, scaleY);
    }

    private static RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }
}
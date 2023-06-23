/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.lite.examples.detection.env;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.util.Pair;

import java.util.Locale;
import java.util.Vector;

/** A class that encapsulates the tedious bits of rendering legible, bordered text onto a canvas. */
public class BorderedText {
  private final Paint interiorPaint;
  private final Paint exteriorPaint;
  String textPos;


  float textSize ; // Başlangıç değeri olarak 12.0 kullanıldı, istediğiniz değeri belirleyebilirsiniz

  /**
   * Creates a left-aligned bordered text object with a white interior, and a black exterior with
   * the specified text size.
   *
   * @param textSize text size in pixels
   */
  public BorderedText(final float textSize, Context context) {
    this(Color.WHITE, Color.BLACK, textSize);
  }

  /**
   * Create a bordered text object with the specified interior and exterior colors, text size and
   * alignment.
   *
   * @param interiorColor the interior text color
   * @param exteriorColor the exterior text color
   * @param textSize text size in pixels
   */
  public BorderedText(final int interiorColor, final int exteriorColor, final float textSize) {
    interiorPaint = new Paint();
    interiorPaint.setTextSize(textSize);
    interiorPaint.setColor(interiorColor);
    interiorPaint.setStyle(Style.FILL);
    interiorPaint.setAntiAlias(false);
    interiorPaint.setAlpha(255);

    exteriorPaint = new Paint();
    exteriorPaint.setTextSize(textSize);
    exteriorPaint.setColor(exteriorColor);
    exteriorPaint.setStyle(Style.FILL_AND_STROKE);
    exteriorPaint.setStrokeWidth(textSize / 8);
    exteriorPaint.setAntiAlias(false);
    exteriorPaint.setAlpha(255);

    this.textSize = textSize;
  }

  public void setTypeface(Typeface typeface) {
    interiorPaint.setTypeface(typeface);
    exteriorPaint.setTypeface(typeface);
  }

  public void drawText(final Canvas canvas, final float posX, final float posY, final String text) {
    canvas.drawText(text, posX, posY, exteriorPaint);
    canvas.drawText(text, posX, posY, interiorPaint);
  }
  private static final float CENTER_THRESHOLD = 0.5f; // Eşik değeri, nesnenin merkezde olduğunu belirlemek için kullanılır

  public String getObjectPosition(float xPos, float totalX) {
    final float finalX = xPos / totalX;
    if (finalX < CENTER_THRESHOLD) {
      textPos = "Solda";
      return "Solda";
    } else {
      textPos = "Sağda";

      return "Sağda";
    }

  }

  public Pair<Float, Float> getStereoRatio(float totalX, float x) {
    float ratio = x / totalX;
    return new Pair<>(1-ratio, ratio);
  }

  public void drawText(
          final Canvas canvas, final float posX, final float posY, String text, Paint bgPaint) {

    float width = exteriorPaint.measureText(text);
    float textSize = exteriorPaint.getTextSize();
    float textX;

    // Ekranı üçe bölerek metni orta bölgeye hizalama
    float screenThird = canvas.getWidth() / 3.0f;

    if (getObjectPosition(posX, canvas.getWidth()).equals("Solda")) {
      // Sol tarafta ise metni orta bölgenin solunda çiz
      textX = screenThird - width / 2.0f;
    } else if (getObjectPosition(posX, canvas.getWidth()).equals("Sağda")) {
      // Sağ tarafta ise metni orta bölgenin sağında çiz
      textX = 2 * screenThird - width / 2.0f;
    } else {
      // Orta bölgede ise metni orta bölgede çiz
      textX = canvas.getWidth() / 2.0f - width / 2.0f;
    }
    if (SpatialVocalizer.shared != null) {
      float x = posX / canvas.getWidth();
      SpatialVocalizer.shared.playText(text, 1.0f - x, x);
    }
    text = getObjectPosition(posX, canvas.getWidth()) + " " + text;

    Paint paint = new Paint(bgPaint);
    paint.setStyle(Paint.Style.FILL);
    paint.setAlpha(160);
    canvas.drawRect(textX, (posY + textSize), (textX + width), posY, paint);
    canvas.drawText(text, textX, (posY + textSize), interiorPaint);
    Pair<Float, Float> ratio = getStereoRatio(canvas.getWidth(), posX);
    Log.d("DRAWTEXT", String.valueOf(posX / canvas.getWidth()));

  }



  public void drawLines(Canvas canvas, final float posX, final float posY, Vector<String> lines) {
    int lineNum = 0;
    for (final String line : lines) {
      drawText(canvas, posX, posY - getTextSize() * (lines.size() - lineNum - 1), line);
      ++lineNum;
    }
  }

  public void setInteriorColor(final int color) {
    interiorPaint.setColor(color);
  }

  public void setExteriorColor(final int color) {
    exteriorPaint.setColor(color);
  }

  public float getTextSize() {
    return textSize;
  }

  public void setAlpha(final int alpha) {
    interiorPaint.setAlpha(alpha);
    exteriorPaint.setAlpha(alpha);
  }

  public void getTextBounds(
      final String line, final int index, final int count, final Rect lineBounds) {
    interiorPaint.getTextBounds(line, index, count, lineBounds);
  }

  public void setTextAlign(final Align align) {
    interiorPaint.setTextAlign(align);
    exteriorPaint.setTextAlign(align);
  }
}

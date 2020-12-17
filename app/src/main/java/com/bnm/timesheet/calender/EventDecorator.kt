package com.bnm.timesheet.calender

import android.R
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

class EventDecorator : Fragment()
{

}
/*class EventDecorator(view: MaterialCalendarView, date: Date?, color: Int) :
        DayViewDecorator {
        private val drawable: Drawable
        private val day: CalendarDay
        private val color: Int
        fun shouldDecorate(day: CalendarDay?): Boolean {
            return if (this.day.equals(day)) {
                true
            } else false
        }

        fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable)
        }

        companion object {
            private fun createTintedDrawable(context: Context, color: Int): Drawable {
                return applyTint(createBaseDrawable(context), color)
            }

            private fun applyTint(drawable: Drawable?, color: Int): Drawable {
                val wrappedDrawable = DrawableCompat.wrap(drawable!!)
                DrawableCompat.setTint(wrappedDrawable, color)
                return wrappedDrawable
            }

            private fun createBaseDrawable(context: Context): Drawable? {
                return ContextCompat.getDrawable(context, R.drawable.day)
            }
        }

        init {
            day = CalendarDay.from(date)
            this.color = color
            drawable = createTintedDrawable(view.getContext(), color)
        }

}*/
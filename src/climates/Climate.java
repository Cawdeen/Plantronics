/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package climates;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import plantronicfx.MainController;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class Climate {
    
    private String name;
    private ArrayList<Season> seasons = new ArrayList<Season>();
    private Season activeSeason;
    
    public Climate(ArrayList<Season> newSeasons)
    {
        for(int i = 0; i < newSeasons.size(); i++)
        {
            addSeason(newSeasons.get(i));
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public ArrayList<Season> getSeasonList()
    {
        return seasons;
    }
    
    public void addSeason(Season seasonToAdd)
    {
        seasons.add(seasonToAdd);
        updateSeasonsMidDates();
    }
    
    public Season getSeason(int index)
    {
        return seasons.get(index);
    }
    
    public int getNumberOfSeasons()
    {
        return seasons.size();
    }
        
    public void updateSeasonsMidDates()
    {
        ArrayList<Season> seasonsList = getSeasonOrder();
        int daysToMid = 185; //start date plus half a year
        if(seasonsList.size() == 1)//one season
        {
            //mid date is start date plus half a year
            return;
            
        }
        else //more than 1 season
        {
            for(int i = 0; i < seasonsList.size(); i++)
            {
                if(i == seasonsList.size()-1)//last index
                {
                    //next season is the first index
                    daysToMid = (int)getSeasonLength(seasonsList.get(i), seasonsList.get(0));
                    
                }
                else
                {
                    //next season is the next index
                    daysToMid = (int)getSeasonLength(seasonsList.get(i), seasonsList.get(i+1));
                }
                daysToMid = daysToMid/2;
                seasonsList.get(i).setDaysToMid(daysToMid);
            }
        }
    }
    
    public ArrayList<Season> getSeasonOrder()
    {
        //blank list of start dates
        ArrayList<Season> orderedSeasons = new ArrayList<Season>();
        if(getNumberOfSeasons()>1)   
        {
            //copy the list
            for(int i = 0; i<getNumberOfSeasons(); i++)
            {
                orderedSeasons.add(getSeason(i));
            }
            //started checking at next index
            Season temp; //for swap
            for (int i = 0; i < getNumberOfSeasons()-1; i++) 
            {
                for (int j = i + 1; j < getNumberOfSeasons(); j++) { 
                    LocalDate d1 = orderedSeasons.get(i).getStartDate();
                    LocalDate d2 = orderedSeasons.get(j).getStartDate();
                    if (d1.getDayOfYear() > d2.getDayOfYear()) 
                    {
                        //perform swap
                        temp = orderedSeasons.get(i);
                        orderedSeasons.set(i, orderedSeasons.get(j));
                        //orderedSeasons.add(i, orderedSeasons.get(j));
                        orderedSeasons.set(j, temp);
                        //orderedSeasons.add(j, rain);
                    }
                }
            }
        }
        else if(getNumberOfSeasons()==1)
        {
            orderedSeasons.add(getSeason(0));
        }
        else
        {
            System.out.println("There are no seasons in the climate.");
        }
        return orderedSeasons;
    }
    
    public ArrayList<Float> getMonthlysForYear(String type) //"temperature" or "humidity"
    {
        ArrayList<Float> dailys = new ArrayList<Float>();
        ArrayList<Float> monthlys = new ArrayList<Float>();
        dailys = getDailysForYear(type);
        
        for(int i = 0; i < 12; i++)//months
        {
            ArrayList<Float> highsForMonth = new ArrayList<Float>();
            int month = i+1;
            for(int j = 0; j < dailys.size(); j++)//days
            {
                int day = j+1;
                Year y = Year.of(2020);
                LocalDate dayDate = y.atDay(day);
                //we're in the current month
                if(dayDate.getMonthValue()==month)
                {
                    highsForMonth.add(dailys.get(j));
                }
            }
            float highsTotal = 0f;
            for(int t = 0; t < highsForMonth.size(); t++)
            {
                highsTotal = highsTotal + highsForMonth.get(t);
            }
            float highAvg = highsTotal/highsForMonth.size();
            monthlys.add(highAvg);
        }
        return monthlys;
    }
    
    public ArrayList<Integer> getMonthlyLowsForYear(String type) //"temperature" or "humidity"
    {
        ArrayList<Integer> dailys = new ArrayList<Integer>();
        ArrayList<Integer> monthlys = new ArrayList<Integer>();
        dailys = getDailyLowsForYear(type);
        
        for(int i = 0; i < 12; i++)//months
        {
            ArrayList<Integer> lowsForMonth = new ArrayList<Integer>();
            int month = i+1;
            for(int j = 0; j < dailys.size(); j++)//days
            {
                int day = j+1;
                Year y = Year.of(2020);
                LocalDate dayDate = y.atDay(day);
                //we're in the current month
                if(dayDate.getMonthValue()==month)
                {
                    lowsForMonth.add(dailys.get(j));
                }
            }
            int lowTotal = 0;
            for(int t = 0; t < lowsForMonth.size(); t++)
            {
                lowTotal = lowTotal + lowsForMonth.get(t);
            }
            int lowAvg = lowTotal/lowsForMonth.size();
            monthlys.add(lowAvg);
        }
        return monthlys;
    }
    
    public ArrayList<Integer> getMonthlyRainForYear()
    {
        ArrayList<Integer> dailys = new ArrayList<Integer>();
        ArrayList<Integer> monthlys = new ArrayList<Integer>();
        dailys = getDailyRainForYear();
        
        for(int i = 0; i < 12; i++)//months
        {
            ArrayList<Integer> rainForMonth = new ArrayList<Integer>();
            int month = i+1;
            for(int j = 0; j < dailys.size(); j++)//days
            {
                int day = j+1;
                Year y = Year.of(2020);
                LocalDate dayDate = y.atDay(day);
                //we're in the current month
                if(dayDate.getMonthValue()==month)
                {
                    rainForMonth.add(dailys.get(j));
                }
            }
            int lowTotal = 0;
            for(int t = 0; t < rainForMonth.size(); t++)
            {
                lowTotal = lowTotal + rainForMonth.get(t);
            }
            int rainAvg = lowTotal/rainForMonth.size();
            monthlys.add(rainAvg);
        }
        return monthlys;
    }
    
    public ArrayList<Integer> getDailyRainForYear()
    {
//      y=a*sin((x-(s/2))/(s/Math.PI))+m
//
//      y= environmental value
//      a= amplitude (half the difference between the minimum and maximum values
//      x= current day in the range
//      s= season length
//      m= the midpoint of the wave (value of the midpoint between min and max)
        
        ArrayList<Integer> dailyRain = new ArrayList<>();
        //get the temps for each month
        for(int i = 1; i <= 365; i++)
        {
            int dayOfYear = i;
            Year y = Year.of(2020);
            LocalDate localDate = y.atDay(dayOfYear);
            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
            //LocalDate localDate = LocalDate.parse(date, sdf);
            Season checkSeason1 = getSeasonByDate(localDate);
            Season checkSeason2 = getNextSeasonByDate(localDate);
            
            Season checkSeason3 = getNextSeasonByDate(checkSeason2.getStartDate().plusDays(1));
            //get rain days
            int r1=0;
            int r2=0;
            r1 = checkSeason1.getRainDays();
            r2 = checkSeason2.getRainDays();
            
            if(r2<r1)
            {
                float a = (r1-r2)/2;
                float s1 = getSeasonLength(checkSeason1, checkSeason2);//days of this season
                float s2 = getSeasonLength(checkSeason2, checkSeason3);//days of next season
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                //float s = s1/2 + s2/2; //days between midpoints of seasons
                float x = i-checkSeason2.getStartDate().getDayOfYear();

                float m = r1 - a;
                int rain = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                dailyRain.add(rain);
            }
            else
            {
                float a = (r2-r1)/2;
                //float s1 = getSeasonLength(checkSeason1, checkSeason2);//days of this season
                //float s2 = getSeasonLength(checkSeason2, checkSeason3);//days of next season
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                //float s = s1/2 + s2/2; //days between midpoints of seasons
                float x = i-checkSeason1.getStartDate().getDayOfYear();
                
                if(x < 0)
                {
                    x = x+365;
                }
                float m = r2 - a;
                int rain = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                dailyRain.add(rain);
            }
            
        }

        return dailyRain;
    }
        
    //im not sure if this is any different than the original
//    public ArrayList<Integer> getDailyHighsForYear(String type) //"temperature" or "humidity"
//    {
////      y=a*sin((x-(s/2))/(s/Math.PI))+m
////
////      y= environmental value
////      a= amplitude (half the difference between the minimum and maximum values
////      x= current day in the range
////      s= season length
////      m= the midpoint of the wave (value of the midpoint between min and max)
//            
//        ArrayList<Integer> dailys = new ArrayList<>();
//        //get the values for each day
//        for(int i = 1; i <= 365; i++)
//        {
//            int dayOfYear = i;
//            Year y = Year.of(2020);
//            LocalDate localDate = y.atDay(dayOfYear);
//            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
//            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
//            //LocalDate localDate = LocalDate.parse(date, sdf);
//            Season checkSeason1 = getSeasonByDate(localDate);
//            Season checkSeason2 = getNextSeasonByDate(localDate);
//            System.out.println("localDate: "+localDate.toString());
//            System.out.println("This season: "+checkSeason1.getName());
//            System.out.println("Next Season "+checkSeason2.getName());
//            
//            int t1=0;
//            int t2=0;
//            
//            if(type == "humidity")
//            {
//                //get humidity
//                t1 = checkSeason1.getHumidRange().getMax();
//                t2 = checkSeason2.getHumidRange().getMax();
//            }
//            else
//            {
//                //get temps
//                t1 = checkSeason1.getTempRange().getMax();
//                t2 = checkSeason2.getTempRange().getMax();
//            }
//            
//            if(t2<t1)
//            {
//                float a = (t1-t2)/2;
//                float s = getSeasonLength(checkSeason1, checkSeason2);//days
//                float x = i-checkSeason2.getStartDate().getDayOfYear();
//                //if(x < 0)
//                //{
//                 //   x = x+365;
//                //    System.out.println(x);
//                //}
//                float m = t1 - a;
//                int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                dailys.add(temp);
//                System.out.println("Daily: "+temp);
//            }
//            else
//            {
//                float a = (t2-t1)/2;
//                float s = getSeasonLength(checkSeason1, checkSeason2);//days
//                float x = i-checkSeason1.getStartDate().getDayOfYear();
//                if(x < 0)
//                {
//                    x = x+365;
//                }
//                float m = t2 - a;
//                int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                dailys.add(temp);
//                System.out.println("Daily: "+temp);
//            }
//        }
//        return dailys;
//    }
    
    //to be used with new season midpoint calculations
    public ArrayList<Float> getDailysForYear(String type) //"temperature" or "humidity"
    {
//      OLD
//      y=a*sin((x-(s/2))/(s/Math.PI))+m
//
//      y= environmental value
//      a= amplitude (half the difference between the minimum and maximum values
//      x= current day in the range
//      s= season length
//      m= the midpoint of the wave (value of the midpoint between min and max)
             
        ArrayList<Float> dailys = new ArrayList<>();
        //get the values for each day
        for(int i = 1; i <= 365; i++)
        {
            int dayOfYear = i;
            Year y = Year.of(2020);
            LocalDate localDate = y.atDay(dayOfYear);
            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
            //LocalDate localDate = LocalDate.parse(date, sdf);
 
            Season checkSeason1 = getFirstSeasonByDate(localDate);
            //Season checkSeason2 = getSecondSeasonByDate(localDate);
            Season checkSeason2 = getSecondSeason(checkSeason1);
            
            float t1=0;
            float t2=0;
            
            if(type == "humidity high")
            {
                //get humidity
                t1 = checkSeason1.getHumidRange().getMax();
                t2 = checkSeason2.getHumidRange().getMax();
            }
            else if(type == "temperature high")
            {
                //get temps
                t1 = checkSeason1.getTempRange().getMax();
                t2 = checkSeason2.getTempRange().getMax();
            }
            else if(type == "humidity low")
            {
                //get humidity
                t1 = checkSeason1.getHumidRange().getMin();
                t2 = checkSeason2.getHumidRange().getMin();
            }
            else if(type == "temperature low")
            {
                //get temps
                t1 = checkSeason1.getTempRange().getMin();
                t2 = checkSeason2.getTempRange().getMin();
            }
            else if(type == "raindays")
            {
                //get raindays
                t1 = checkSeason1.getRainDays();
                t2 = checkSeason2.getRainDays();
            }
            else if(type == "daylight")
            {
                //get daylights
                t1 = checkSeason1.getDayLight();
                t2 = checkSeason2.getDayLight();
            }
            int d1 = checkSeason1.getMidDate().getDayOfYear();
            int d2 = checkSeason2.getMidDate().getDayOfYear();
            float x = 0;
            float w = (checkSeason1.getDaysToMid()+checkSeason2.getDaysToMid()/2)/10; //half the weeks from mid to mid
            if(d2 < d1) //second season middate passes jan 1st - Not sure this makes sense
            {
                //get the number of days from d1 to d2:
                        //get the days from jan 1st to d2 (actually just d2)
                        //get the days from d1 to end of year
                        //add them to the get the length from d1-d2
                        //get the mid point
                int dd2 = d2;//get the days from jan 1st to d2 (actually just d2)
                int dd1 = 365 - d1;//get the days from d1 to end of year
                int ddm = (dd1+dd2)/2;//add them to the get the length from d1-d2 then get the mid point
                
                if(i > d2 && i < checkSeason1.getStartDate().getDayOfYear()) //we havent passed jan 1st yet
                {
                    //find out where we are in that
                    x = (i-d1)-ddm; //will give negative or positive number
                    if(x<((dd1+dd2)*-1))
                    {
                        x = x + 365;
                    }
                    
                    else
                    {
                        ddm = (d2-d1)/2;//get the number of days from d1 to d2: (d2-d1) -days to midpoint
                        x = (i-d1)-ddm;//find out where we are in that 
                    }
 
                }
                else if(i > d2)
                {
                    //find out where we are in that
                    x = (i-d1)-ddm; //will give negative or positive number
                }
                else //current day has passed jan 1st
                {
                    //find out where we are in that
                    x = (i+dd1)-ddm; //will give negative or positive number
                }
            }
            else //both are in the same year
            {
                //get the number of days from d1 to d2: (d2-d1)
                //get mid point (d2 - (days between/2))
                //find out where we are in that 
                int ddm = (d2-d1)/2;//get the number of days from d1 to d2: (d2-d1) -days to midpoint
                x = (i-d1)-ddm;//find out where we are in that 
            }
            if(t2<t1) //slope going down
            {
                float a = (t1-t2);
                float m = t2;
                //float s = getSeasonLength(checkSeason1, checkSeason2)/4;//weeks
//                  *************
//                int d1 = checkSeason1.getMidDate().getDayOfYear();
//                int d2 = checkSeason2.getMidDate().getDayOfYear();
//                float x = 0;
//                if(d2 < d1) //second season middate passes jan 1st
//                {
//                    //get the number of days from d1 to d2:
//                            //get the days from jan 1st to d2 (actually just d2)
//                            //get the days from d1 to end of year
//                            //add them to the get the length from d1-d2
//                            //get the mid point
//                    int dd2 = d2;//get the days from jan 1st to d2 (actually just d2)
//                    int dd1 = 365 - d1;//get the days from d1 to end of year
//                    int ddm = (dd1+dd2)/2;//add them to the get the length from d1-d2 then get the mid point
//                    
//                    if(i > d2) //we havent passed jan 1st yet
//                    {
//                        //find out where we are in that
//                        x = (i-d1)-ddm; //will give negative or positive number
//                    }
//                    else //current day has passed jan 1st
//                    {
//                        //find out where we are in that
//                        x = (i+dd1)-ddm; //will give negative or positive number
//                    }
//                }
//                else //both are in the same year
//                {
//                    //get the number of days from d1 to d2: (d2-d1)
//                    //get mid point (d2 - (days between/2))
//                    //find out where we are in that 
//                    int ddm = (d2-d1)/2;//get the number of days from d1 to d2: (d2-d1) -days to midpoint
//                    x = (i-d1)-ddm;//find out where we are in that 
//                }
//                *************
                //float x = checkSeason1.getMidDate().getDayOfYear()
                //float x = i-checkSeason2.getStartDate().getDayOfYear();
                //if(x < 0)
                //{
                 //   x = x+365;
                //    System.out.println(x);
                //}
                
                float temp = (int) ((a/(1+Math.pow(Math.E,(x/w))))+m);
                //y=(a)/(1+ℯ^(x/w))+m
                //int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                dailys.add(temp);
                
            }
            else //slope going up
            {
                float a = (t2-t1);
                float m = t1;
//                if(x < 0)
//                {
//                    x = x+365;
//                }
                //y=(a)/(1+ℯ^(-x/w))+m
                float temp = (int) ((a/(1+Math.pow(Math.E,(-x/w))))+m);
                //int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                dailys.add(temp);
            }
            
        }
        return dailys;
    }
    //currently works but is buggy at certain season lengths
    
//    //original
//    public ArrayList<Integer> getDailyHighsForYear(String type) //"temperature" or "humidity"
//    {
////      y=a*sin((x-(s/2))/(s/Math.PI))+m
////
////      y= environmental value
////      a= amplitude (half the difference between the minimum and maximum values
////      x= current day in the range
////      s= season length
////      m= the midpoint of the wave (value of the midpoint between min and max)
//             
//        ArrayList<Integer> dailys = new ArrayList<>();
//        //get the values for each day
//        for(int i = 1; i <= 365; i++)
//        {
//            int dayOfYear = i;
//            Year y = Year.of(2020);
//            LocalDate localDate = y.atDay(dayOfYear);
//            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
//            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
//            //LocalDate localDate = LocalDate.parse(date, sdf);
// 
//            Season checkSeason1 = getFirstSeasonByDate(localDate);
//            //Season checkSeason2 = getSecondSeasonByDate(localDate);
//            Season checkSeason2 = getNextSeasonByDate(localDate);
//            System.out.println("localDate: "+localDate.toString());
//            System.out.println("This season: "+checkSeason1.getName());
//            System.out.println("Next Season "+checkSeason2.getName());
//            
//            int t1=0;
//            int t2=0;
//            
//            if(type == "humidity")
//            {
//                //get humidity
//                t1 = checkSeason1.getHumidRange().getMax();
//                t2 = checkSeason2.getHumidRange().getMax();
//            }
//            else
//            {
//                //get temps
//                t1 = checkSeason1.getTempRange().getMax();
//                t2 = checkSeason2.getTempRange().getMax();
//            }
//            
//            if(t2<t1)
//            {
//                float a = (t1-t2)/2;
//                float s = getSeasonLength(checkSeason1, checkSeason2);//days
//                float x = i-checkSeason2.getStartDate().getDayOfYear();
//                //if(x < 0)
//                //{
//                 //   x = x+365;
//                //    System.out.println(x);
//                //}
//                float m = t1 - a;
//                int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                dailys.add(temp);
//                System.out.println("Daily: "+temp);
//            }
//            else
//            {
//                float a = (t2-t1)/2;
//                float s = getSeasonLength(checkSeason1, checkSeason2);//days
//                float x = i-checkSeason1.getStartDate().getDayOfYear();
//                if(x < 0)
//                {
//                    x = x+365;
//                }
//                float m = t2 - a;
//                int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                dailys.add(temp);
//                System.out.println("Daily: "+temp);
//            }
//        }
//        return dailys;
//    }
    //currently works but is buggy at certain season lengths
    public ArrayList<Integer> getDailyLowsForYear(String type) //"temperature" or "humidity"
    {
//      y=a*sin((x-(s/2))/(s/Math.PI))+m
//
//      y= environmental value
//      a= amplitude (half the difference between the minimum and maximum values
//      x= current day in the range
//      s= season length
//      m= the midpoint of the wave (value of the midpoint between min and max)
        
        ArrayList<Integer> dailyTemps = new ArrayList<>();
        //get the temps for each month
        for(int i = 1; i <= 365; i++)
        {
            int dayOfYear = i;
            Year y = Year.of(2020);
            LocalDate localDate = y.atDay(dayOfYear);
            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
            //LocalDate localDate = LocalDate.parse(date, sdf);
            Season checkSeason1 = getSeasonByDate(localDate);
            Season checkSeason2 = getNextSeasonByDate(localDate);
            
            int t1=0;
            int t2=0;
            
            if(type == "humidity")
            {
                //get humidity
                t1 = checkSeason1.getHumidRange().getMin();
                t2 = checkSeason2.getHumidRange().getMin();
            }
            else
            {
                //get temps
                t1 = checkSeason1.getTempRange().getMin();
                t2 = checkSeason2.getTempRange().getMin();
            }
            
            if(t2<t1)
            {
                float a = (t1-t2)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason2.getStartDate().getDayOfYear();
                //if(x < 0)
                //{
                 //   x = x+365;
                //    System.out.println(x);
                //}
                float m = t1 - a;
                int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                dailyTemps.add(temp);
            }
            else
            {
                float a = (t2-t1)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason1.getStartDate().getDayOfYear();
                if(x < 0)
                {
                    x = x+365;
                }
                float m = t2 - a;
                int temp = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                dailyTemps.add(temp);
            }
            
        }
        return dailyTemps;
    }
    
    public ArrayList<Float> getMonthlyDaylightForYear()
    {
        ArrayList<Float> dailys = new ArrayList<Float>();
        ArrayList<Float> monthlys = new ArrayList<Float>();
        dailys = getDailyDaylightForYear();
        
        for(int i = 0; i < 12; i++)//months
        {
            ArrayList<Float> daylightsForMonth = new ArrayList<Float>();
            int month = i+1;
            for(int j = 0; j < dailys.size(); j++)//days
            {
                int day = j+1;
                Year y = Year.of(2020);
                LocalDate dayDate = y.atDay(day);
                //we're in the current month
                if(dayDate.getMonthValue()==month)
                {
                    daylightsForMonth.add(dailys.get(j));
                }
            }
            float daylightsTotal = 0;
            for(int t = 0; t < daylightsForMonth.size(); t++)
            {
                daylightsTotal = daylightsTotal + daylightsForMonth.get(t);
            }
            float daylightsAvg = daylightsTotal/daylightsForMonth.size();
            monthlys.add(daylightsAvg);
        }
        return monthlys;
    }
    
    public ArrayList<Float> getDailyDaylightForYear()
    {
//      y=a*sin((x-(s/2))/(s/Math.PI))+m
//
//      y= environmental value
//      a= amplitude (half the difference between the minimum and maximum values
//      x= current day in the range
//      s= season length
//      m= the midpoint of the wave (value of the midpoint between min and max)
        
        ArrayList<Float> daylights = new ArrayList<>();
        //get the temps for each month
        for(int i = 1; i <= 365; i++)
        {
            int dayOfYear = i;
            Year y = Year.of(2020);
            LocalDate localDate = y.atDay(dayOfYear);
            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
            //LocalDate localDate = LocalDate.parse(date, sdf);
            Season checkSeason1 = getSeasonByDate(localDate);
            Season checkSeason2 = getNextSeasonByDate(localDate);
            float t1 = checkSeason1.getDayLight();
            float t2 = checkSeason2.getDayLight();
            if(t2<t1)
            {
                float a = (t1-t2)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason2.getStartDate().getDayOfYear();
                //if(x < 0)
                //{
                 //   x = x+365;
                //    System.out.println(x);
                //}
                float m = t1 - a;
                float dl = (float) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                daylights.add(dl);
            }
            else
            {
                float a = (t2-t1)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason1.getStartDate().getDayOfYear();
                if(x < 0)
                {
                    x = x+365;
                }
                float m = t2 - a;
                float dl = (float) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                daylights.add(dl);
            }
            
        }
        return daylights;
    }
    
    public LocalTime getTodaySunset()
    {
        ArrayList<Float> sunsets = new ArrayList<>();
        
        for(int i = 1; i <= 365; i++)
        {
            int dayOfYear = i;
            Year y = Year.of(2020);
            LocalDate localDate = y.atDay(dayOfYear);
            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
            //LocalDate localDate = LocalDate.parse(date, sdf);
            Season checkSeason1 = getSeasonByDate(localDate);
            Season checkSeason2 = getNextSeasonByDate(localDate);
            int s1 = checkSeason1.getLightDuration().getEnd1();
            int s2 = checkSeason1.getLightDuration().getEnd2();
            float sunsetSeconds1 = (float)(s1*3600f) + ((s2/60f)*3600f);
            s1 = checkSeason2.getLightDuration().getEnd1();
            s2 = checkSeason2.getLightDuration().getEnd2();
            float sunsetSeconds2 = (float)(s1*3600f) + ((s2/60f)*3600f);
            
            if(sunsetSeconds2<sunsetSeconds1)
            {
                float a = (sunsetSeconds1-sunsetSeconds2)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason2.getStartDate().getDayOfYear();
                //if(x < 0)
                //{
                 //   x = x+365;
                //    System.out.println(x);
                //}
                float m = sunsetSeconds1 - a;
                float dl = (float) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                sunsets.add(dl);
            }
            else
            {
                float a = (sunsetSeconds2-sunsetSeconds1)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason1.getStartDate().getDayOfYear();
                if(x < 0)
                {
                    x = x+365;
                }
                float m = sunsetSeconds2 - a;
                float dl = (float) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                sunsets.add(dl);
            }
        }
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);
        float sec = sunsets.get(today); 
        LocalTime sunset = LocalTime.ofSecondOfDay((int)sec);
        return sunset;
    }
    
    public LocalTime getTodaySunrise()
    {
        ArrayList<Float> sunrises = new ArrayList<>();
        
        for(int i = 1; i <= 365; i++)
        {
            int dayOfYear = i;
            Year y = Year.of(2020);
            LocalDate localDate = y.atDay(dayOfYear);
            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
            //LocalDate localDate = LocalDate.parse(date, sdf);
            Season checkSeason1 = getSeasonByDate(localDate);
            Season checkSeason2 = getNextSeasonByDate(localDate);
            int s1 = checkSeason1.getLightDuration().getStart1();
            int s2 = checkSeason1.getLightDuration().getStart2();
            float sunriseSeconds1 = (float)(s1*3600f) + ((s2/60f)*3600f);
            s1 = checkSeason2.getLightDuration().getStart1();
            s2 = checkSeason2.getLightDuration().getStart2();
            float sunriseSeconds2 = (float)(s1*3600f) + ((s2/60f)*3600f);
            
            if(sunriseSeconds2<sunriseSeconds1)
            {
                float a = (sunriseSeconds1-sunriseSeconds2)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason2.getStartDate().getDayOfYear();
                //if(x < 0)
                //{
                 //   x = x+365;
                //    System.out.println(x);
                //}
                float m = sunriseSeconds1 - a;
                float dl = (float) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                sunrises.add(dl);
            }
            else
            {
                float a = (sunriseSeconds2-sunriseSeconds1)/2;
                float s = getSeasonLength(checkSeason1, checkSeason2);//days
                float x = i-checkSeason1.getStartDate().getDayOfYear();
                if(x < 0)
                {
                    x = x+365;
                }
                float m = sunriseSeconds2 - a;
                float dl = (float) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
                sunrises.add(dl);
            }
        }
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);
        float sec = sunrises.get(today); 
        LocalTime sunrise = LocalTime.ofSecondOfDay((int)sec);
        return sunrise;
    }
    
//    public ArrayList<Integer> getDailyRainForYear()
//    {
////      y=a*sin((x-(s/2))/(s/Math.PI))+m
////
////      y= environmental value
////      a= amplitude (half the difference between the minimum and maximum values
////      x= current day in the range
////      s= season length
////      m= the midpoint of the wave (value of the midpoint between min and max)
//        
//        ArrayList<Integer> dailyRain = new ArrayList<>();
//        //get the temps for each month
//        for(int i = 1; i <= 365; i++)
//        {
//            int dayOfYear = i;
//            Year y = Year.of(2020);
//            LocalDate localDate = y.atDay(dayOfYear);
//            //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/yyyy");
//            //String date = String.valueOf(i+1) + "/" + String.valueOf(1) + "/" + String.valueOf(2020);
//            //LocalDate localDate = LocalDate.parse(date, sdf);
//            Season checkSeason1 = getSeasonByDate(localDate);
//            Season checkSeason2 = getNextSeasonByDate(localDate);
//            Season prevSeason = getPrevSeasonByDate(localDate);
//            Season checkSeason3 = getNextSeasonByDate(checkSeason2.getStartDate().plusDays(1));
//            float s1 = getSeasonLength(checkSeason1, checkSeason2);//days of this season
//            float midOfSeason1 =  checkSeason1.getStartDate().getDayOfYear()+(s1/2f);
//            //if(midOfSeason1>365)
//           // {
//            //    midOfSeason1 = midOfSeason1-365;
//           // }
//            System.out.println("midOfSeason1 "+midOfSeason1);
//            System.out.println("i: "+i);
//            if(i>midOfSeason1)//if we are past the current seasons half way point
//            {              
//                //get rain days
//                int r1=0;
//                int r2=0;
//                r1 = checkSeason1.getRainDays();
//                r2 = checkSeason2.getRainDays();
//                if(r2<r1)
//                {
//                    float a = (r1-r2)/2;
//                    float s2 = getSeasonLength(checkSeason2, checkSeason3);//days of next season
//                    float s = s1/2 + s2/2; //days between midpoints of seasons
//                    float x = i-checkSeason1.getStartDate().getDayOfYear()+s1;//subtract the number that is the midpoint of the season
//                    float m = r1 - a;
//                    int rain = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                    dailyRain.add(rain);
//                }
//                else
//                {
//                    float a = (r2-r1)/2;
//                    float s2 = getSeasonLength(checkSeason2, checkSeason3);//days of next season
//                    float s = s1/2 + s2/2; //days between midpoints of seasons
//                    float x = i-checkSeason1.getStartDate().getDayOfYear()+s1;//subtract the number that is the midpoint of the season
//                    float m = r2 - a;
//                    int rain = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                    dailyRain.add(rain);
//                }
//            }
//            else//still in the first half of the season. We need to use the prev season as the first season, this season as the second
//            {
//                //get rain days
//                int r1=0;
//                int r2=0;
//                r1 = prevSeason.getRainDays();
//                r2 = checkSeason1.getRainDays();
//                if(r2<r1)
//                {
//                    float a = (r1-r2)/2;
//                    float s2 = getSeasonLength(prevSeason, checkSeason1);//days of next season
//                    float s = s1/2 + s2/2; //days between midpoints of seasons
//                    float x = i-prevSeason.getStartDate().getDayOfYear()+s2;//subtract the number that is the midpoint of the previous season
//                    float m = r1 - a;
//                    int rain = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                    dailyRain.add(rain);
//                }
//                else
//                {
//                    float a = (r2-r1)/2;
//                    float s2 = getSeasonLength(prevSeason, checkSeason1);//days of next season
//                    float s = s1/2 + s2/2; //days between midpoints of seasons
//                    float x = i-prevSeason.getStartDate().getDayOfYear()+s2;//subtract the number that is the midpoint of the previous season
//                    float m = r2 - a;
//                    int rain = (int) (a * Math.sin((x-(s/2))/(s/Math.PI))+m);
//                    dailyRain.add(rain);
//                }
//            }
//        }
//        //for(int i = 0; i < dailyRain.size(); i++)
//        //{
//        //    System.out.println("i:"+i + " " + dailyRain.get(i));
//        //}
//        return dailyRain;
//    }
    
/*  IS THIS USED? IS LIGHTDURATION USED INSTEAD?
    public DaylightTime getTodaysLightTime()
    {
        DaylightTime lt = new DaylightTime();//new lighttime (return value)
        ArrayList<Float> daylights = getDailyDaylightForYear(); //get all daylight lengths from method
        Date today = new Date();                                //dates to get todays day
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);          //get the number of the day of year 
        float todaysDaylight = daylights.get(dayOfYear);        //use that number to get the daylight length from the array
        
        return lt;
    }
*/
    
    //calculate the length of daylight for current settings pane
    public String calculateDayLength() //String
    {
        String dayLength = "";
        int startHr = activeSeason.getLightDuration().getStart1();
        int startMn = activeSeason.getLightDuration().getStart2();
        int endHr = activeSeason.getLightDuration().getEnd1();
        int endMn = activeSeason.getLightDuration().getEnd2();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn);
        String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn);
        try {
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 86400000 + elapsed;
            }
            int hours = (int) Math.floor(elapsed / 3600000);
            int minutes = (int) Math.floor((elapsed - hours * 3600000) / 60000);
            dayLength = String.valueOf(hours + " hours, " + minutes + " minutes");
          
        } catch (ParseException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dayLength;
    }
    
    //get length of current season
    public float getSeasonLength(Season s, Season ns)
    {
        //long months = 0;
        long days = 0;
        Season currentSeason = s;
        Season nextSeason = ns;
        int startMonth = currentSeason.getStartDate().getMonthValue();
        int startDay = currentSeason.getStartDate().getDayOfMonth();
        int endMonth = nextSeason.getStartDate().getMonthValue();
        int endDay = nextSeason.getStartDate().getDayOfMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("MM:dd");
        String seasonStart = String.valueOf(startMonth) + ":" + String.valueOf(startDay);
        String seasonEnd = String.valueOf(endMonth) + ":" + String.valueOf(endDay);
        try {
            Date d1 = sdf.parse(seasonStart);
            Date d2 = sdf.parse(seasonEnd);
            long elapsed = d2.getTime() - d1.getTime(); //elapsed time in milliseconds
            //if the end time is less than the start time, then the end time must be the next year. Subtract the difference from a full year length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 31536000000L + elapsed;
            }
            days = elapsed / 86400000L;
            //months = elapsed / 2629800000L;  
        } catch (ParseException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (float)days;
    }
    
    public Season getNextSeasonByDate(LocalDate date)
    {
        Season nextSeason = null;
        ArrayList<Season> seasonsList = getSeasonOrder();
        Season currentSeason = getSeasonByDate(date);
        for(int i = 0; i < seasonsList.size(); i++)
        {
            if(seasonsList.get(i) == currentSeason)
            {
                if(i != seasonsList.size()-1)
                {
                    nextSeason = seasonsList.get(i+1);
                }
                else
                {
                    nextSeason = seasonsList.get(0);
                }
            }
        }
        
        return nextSeason;
    }
    
    public Season getPrevSeasonByDate(LocalDate date)
    {
        Season prevSeason = null;
        ArrayList<Season> seasonsList = getSeasonOrder();
        Season currentSeason = getSeasonByDate(date);
        for(int i = 0; i < seasonsList.size(); i++)
        {
            if(seasonsList.get(i) == currentSeason)
            {
                if(i != 0)
                {
                    //previous season in the list
                    prevSeason = seasonsList.get(i-1);
                }
                else
                {
                    //last season in the list
                    prevSeason = seasonsList.get(seasonsList.size()-1);
                }
            }
        }
        return prevSeason;
    }
    
    public Season getFirstSeasonByDate(LocalDate date)
    {
        //get seasons in correct order
        ArrayList<Season> orderedSeasons = getSeasonOrder();
        //default to the first season
        Season currentSeason = orderedSeasons.get(0);
        Season firstSeasonOrdered = orderedSeasons.get(0);
        Season firstSeason = orderedSeasons.get(0);
        //set date variables
        LocalDate cDate = date;
        LocalDate firstDate = orderedSeasons.get(0).getStartDate();
        if(orderedSeasons.size()>1)//more than 1 season
        {
            for(int i=0; i<orderedSeasons.size(); i++) //go through each season
            {
                LocalDate d = orderedSeasons.get(i).getStartDate();
                LocalDate dm = orderedSeasons.get(i).getMidDate();

                //if the season being checked (start date) is before today, thats the first season (so far)
                if(d.getDayOfYear() < cDate.getDayOfYear())
                {
                    firstSeasonOrdered = orderedSeasons.get(i);

                    if(dm.getDayOfYear()>d.getDayOfYear())//the mid date day of year is larger than the start date day of year
                    {
                        //if the mid date day of year is also larger than today day of year
                        if(dm.getDayOfYear()>cDate.getDayOfYear())
                        {
                            //then we are in the first half of the second season 
                            //(which means the first season of the comparison must be the previous season)
                            if(i>0)//we are not on the first index
                            {
                                firstSeasonOrdered = orderedSeasons.get(i-1);
                                break;
                            }
                            else//we are on the first index, the first season is the last index
                            {
                                firstSeasonOrdered = orderedSeasons.get(orderedSeasons.size()-1);
                                break;
                            }
                        }  
                        //else the mid date day of year is less than today
                        else
                        {
                            //we MIGHT be the second half of the first season
                            if(orderedSeasons.size()>i+1)//if theres another index
                            {
                                //if the start date of the next index is after today
                                if(orderedSeasons.get(i+1).getStartDate().isAfter(cDate))
                                {
                                    //we are actually in the second half of the forst season
                                    firstSeasonOrdered = orderedSeasons.get(i);
                                    break;
                                }
                                else
                                {
                                    //we aint. Do nothing and just let the for loop go
                                }
                            }
                        }
                    }
                    else//the mid date day of year is smaller number than the start date day of year (passes Jan 1st)
                    {
                        //we are in the first half of the second season
                        //the first season is the previous index
                        if(i>0)//we are not on the first index
                        {
                            firstSeasonOrdered = orderedSeasons.get(i-1);
                            break;
                        }
                        else//we are on the first index, the first season is the last index
                        {
                            firstSeasonOrdered = orderedSeasons.get(orderedSeasons.size()-1);
                            break;
                        }
                    }
                }
                else //the start date is after today
                {
                    //There are no more seasons. All start dates are after today, which means the first season is the last in the list
                    if(i+1 >= orderedSeasons.size())
                    {
                        firstSeasonOrdered = orderedSeasons.get(orderedSeasons.size()-1);
                    }
                }
            }
        }
       
        for(int i=0; i < getNumberOfSeasons(); i++)
        {
            if(getSeason(i) == firstSeasonOrdered)
            {
                currentSeason = getSeason(i);
                break;
            }
        }
        return currentSeason;
    }
    
    public Season getSecondSeason(Season firstSeason)
    {
        //get seasons in correct order
        ArrayList<Season> orderedSeasons = getSeasonOrder();
        Season secondSeason = new Season();
        for(int i=0; i < getNumberOfSeasons(); i++)
        {
            if(orderedSeasons.get(i) == firstSeason)
            {
                if(getNumberOfSeasons() > i+1)
                {
                    secondSeason = orderedSeasons.get(i+1);
                }
                else
                {
                    secondSeason = orderedSeasons.get(0);
                }
            }
        }
        return secondSeason;
    }
    
    //return the current season
    public Season getSeasonByDate(LocalDate date)
    {
        //get seasons in correct order
        ArrayList<Season> orderedSeasons = getSeasonOrder();
        //default to the first season
        Season currentSeason = orderedSeasons.get(0);
        Season currentSeasonOrdered = orderedSeasons.get(0);
        //set date variables
        LocalDate cDate = date;
        LocalDate firstDate = orderedSeasons.get(0).getStartDate();
        //if the first season start date is after today, then the current season is the last season of the year
        if(firstDate.getDayOfYear() > cDate.getDayOfYear())
        {
            currentSeasonOrdered = orderedSeasons.get(orderedSeasons.size()-1);
        }
        else
        {
            for(int i=0; i<orderedSeasons.size(); i++)
            {
                LocalDate d = orderedSeasons.get(i).getStartDate();
                //if the season being checked is before today, thats the current season (so far)
                if(d.getDayOfYear() < cDate.getDayOfYear())
                {
                    currentSeasonOrdered = orderedSeasons.get(i);
                }
            }
        }
        for(int i=0; i < getNumberOfSeasons(); i++)
        {
            if(getSeason(i) == currentSeasonOrdered)
            {
                currentSeason = getSeason(i);
                break;
            }
        }
        return currentSeason;
    }
}

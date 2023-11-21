package com.example.postmachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.FocusFinder
import android.view.MenuInflater
import android.view.View
import android.view.WindowId.FocusObserver
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    lateinit var line:List<Section>;
    lateinit var commands:List<Operation>;
    var selectInex:Int?=null;
    var lineSize:Int=100;
    var increment:Int?=null;
    var frameDelay:Int=500;
    var stopCheck:Boolean=false;
    var lastCommandId=0;
    lateinit var scroll:HorizontalScrollView;
    lateinit var layoutContainer:LinearLayout;
    lateinit var thread:Thread;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_PostMachine)
        selectInex=lineSize/2;
        line= listOf<Section>();
        commands= listOf<Operation>();
        for (i in -(lineSize/2)..lineSize/2){
            line+=Section(i);
        }
        /*
        Commands
         */
        commands+=Operation(0,0,1);
        commands+=Operation(1,0,2);
        commands+=Operation(2,-1,2);
        //
        AddListeners()
    }
    fun AddListeners(){
        val tv:TextView=findViewById(R.id.textView2);
        layoutContainer=findViewById(R.id.sectionsWrap);
        DrawLine()
        findViewById<ImageButton>(R.id.playBtn).setOnClickListener{
            stopCheck=false;
            lastCommandId=0;
            var runnable:Runnable =Runnable{
                tv.post{
                    findViewById<ImageButton>(R.id.playBtn).isEnabled=false;
                }
                while (commands[lastCommandId].comand!=-1&&!stopCheck) {
                    tv.post {
                        CommandAplly(lastCommandId);
                        var link =commands[lastCommandId].link!! as Int
                        lastCommandId= commands[link].number!!;
                        DrawLine()
                    }
                    Thread.sleep(frameDelay.toLong());
                }
                tv.post {
                    CommandAplly(lastCommandId);
                    lastCommandId=0;
                    findViewById<ImageButton>(R.id.playBtn).isEnabled=true;
                }
            };
            thread=Thread(runnable)
            thread.start();

        }
        findViewById<ImageButton>(R.id.refreshBtn).setOnClickListener{
            stopCheck=true;
            selectInex=lineSize/2;
            for (i in line.indices)
                line[i].checked=false;
            DrawLine()
            Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageButton>(R.id.nextStep).setOnClickListener{
            stopCheck=false;
            if( lastCommandId in 0..commands.indices.last-1)
            {
                CommandAplly(lastCommandId);
                var link =commands[lastCommandId].link!! as Int
                lastCommandId= commands[link].number!!;
                DrawLine()
            }
            else
                lastCommandId=0;
        }
        findViewById<ImageButton>(R.id.moreBtn).setOnClickListener{
            val settings:ConstraintLayout=findViewById(R.id.settings);
            settings.visibility=View.VISIBLE;
        }
        findViewById<Button>(R.id.applyBtn).setOnClickListener{
            val settings:ConstraintLayout=findViewById(R.id.settings);
            settings.visibility=View.GONE;
            lineSize=findViewById<EditText>(R.id.cellsContET).text.toString().toInt();
            frameDelay=findViewById<EditText>(R.id.delayET).text.toString().toInt();
            UpdateLine();
            DrawLine();
        }
        tv.text="";
    }
    fun UpdateLine(){
        selectInex=lineSize/2;
        line= listOf<Section>();
        commands= listOf<Operation>();
        for (i in -(lineSize/2)..lineSize/2){
            line+=Section(i);
        }
        commands+=Operation(0,0,1);
        commands+=Operation(1,0,2);
        commands+=Operation(2,-1,2);
    }

    fun CommandAplly(index:Int){
        if(commands[index].comand==0) {
            if(selectInex!! > 0)
                selectInex = selectInex!!-1;
            else
                selectInex=lineSize
        }
        if(commands[index].comand==1) {
            if(selectInex!!<lineSize)
                selectInex = selectInex!!+1;
            else
                selectInex = -0;
        }
        if(selectInex in line.indices)
            if(selectInex!=lineSize&&selectInex!=0)
            {
                if(commands[index].comand==2) {
                    line[selectInex!!].checked=true;
                }
                if(commands[index].comand==3) {
                    line[selectInex!!].checked=false;
                }
            }else
            {
                if(selectInex==lineSize&&commands[index].comand==2)
                    line[0].checked=true;
                if(selectInex==0&&commands[index].comand==3) {
                    line[lineSize].checked=false;
                }
            }
        if(commands[lastCommandId].comand==-1)
        {
            lastCommandId=0;
            Toast.makeText(this, "Выполненено", Toast.LENGTH_SHORT).show()
        }



    }
    fun DrawLine(){
        increment=((lineSize/50)*25).toInt();
        layoutContainer.removeAllViews();
        for (i in -(lineSize/2)..lineSize/2){
            val newRelativeLayout = layoutInflater.inflate(R.layout.section_card, null) as RelativeLayout
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0,0,5,0);

            newRelativeLayout.layoutParams=params
            ((newRelativeLayout.getChildAt(0)as TextView)).text=i.toString();

            layoutContainer.addView(newRelativeLayout)
            newRelativeLayout.setId(i);
            //(increment!! *50)
            ((newRelativeLayout.getChildAt(0)as TextView)).text=newRelativeLayout.id.toString()
            if(line[i+increment!!].checked)
                    (newRelativeLayout.getChildAt(1)as RelativeLayout).getChildAt(0).visibility=View.VISIBLE;
            if((i+increment!!)==selectInex)
                (newRelativeLayout.getChildAt(2)as RelativeLayout).visibility=View.VISIBLE
            (newRelativeLayout.getChildAt(1)as RelativeLayout).setOnClickListener {
                line[i+increment!!].Check();
                DrawLine();
            }

        }

    }
}
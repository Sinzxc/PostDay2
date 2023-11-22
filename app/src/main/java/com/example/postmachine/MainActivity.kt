package com.example.postmachine

import android.graphics.Path.Op
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
    lateinit var commands:MutableList<Operation>;
    var selectInex:Int?=null;
    var lineSize:Int=100;
    var increment:Int?=null;
    var frameDelay:Int=500;
    var stopCheck:Boolean=false;
    var lastCommandId=0;
    lateinit var scroll:HorizontalScrollView;
    lateinit var layoutContainer:LinearLayout;
    lateinit var thread:Thread;
    var deleteCount=0;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_PostMachine)
        selectInex=lineSize/2;
        line= listOf<Section>();
        commands= mutableListOf<Operation>();
        for (i in -(lineSize/2)..lineSize/2){
            line+=Section(i);
        }
        TestComands()
        AddListeners()
        DrawCommand()
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
                var temp=0;
                if(lastCommandId!=0)
                {
                    temp=1;
                }
                else
                    temp=0;
                for (i in commands.indices) {
                    tv.post {
                        stopCheck=false;
                        if( lastCommandId in 0..commands.indices.last)
                        {
                            if(commands[lastCommandId].link!=null)
                            {
                                CommandAplly(lastCommandId);
                                DrawLine()
                                val link =commands[lastCommandId].link
                                lastCommandId= link!!-deleteCount;
                            }
                            else
                            {
                                lastCommandId=0;
                                Toast.makeText(this, "Не заданы команды", Toast.LENGTH_SHORT).show()
                                stopCheck=true;
                            }

                        }
                        else
                            lastCommandId=0;


                    }
                    Thread.sleep(frameDelay.toLong());
                }
                tv.post {
                    findViewById<ImageButton>(R.id.playBtn).isEnabled=true;
                }
            };
            thread=Thread(runnable)
            thread.start();

        }
        findViewById<ImageButton>(R.id.refreshBtn).setOnClickListener{
            stopCheck=true;
            lastCommandId=0;
            selectInex=lineSize/2;
            for (i in line.indices)
                line[i].checked=false;
            DrawLine()
            Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT).show()
            for(index in commands.indices)
            {
                var commandsWrap:LinearLayout=findViewById(R.id.commandWrap);
                    (commandsWrap.getChildAt(index)as RelativeLayout).getChildAt(3).visibility=View.INVISIBLE;
            }

        }
        findViewById<ImageButton>(R.id.nextStep).setOnClickListener{
            stopCheck=false;
            if( lastCommandId in 0..commands.indices.last)
            {
                if(commands[lastCommandId].link!=null)
                {
                    CommandAplly(lastCommandId);
                    DrawLine()
                    val link =commands[lastCommandId].link
                    lastCommandId= link!!-deleteCount;
                }
                else
                {
                    lastCommandId=0;
                    Toast.makeText(this, "Не заданы команды", Toast.LENGTH_SHORT).show()
                    stopCheck=true;
                }

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
    fun TestComands(){
        /*
       Commands
        */
        commands+=Operation(0,0,1);
        commands+=Operation(1,1,2);
        commands+=Operation(2,2,3);
        commands+=Operation(3,2,4);
        commands+=Operation(4,2,5);
        commands+=Operation(5,2,6);
        commands+=Operation(6,4,16);
        commands[commands.size-1].link2=0;
        commands+=Operation(7,2,8);
        commands+=Operation(8,2,9);
        commands+=Operation(9,2,10);
        commands+=Operation(10,2,11);
        commands+=Operation(11,2,12);
        commands+=Operation(12,2,13);
        commands+=Operation(13,2,14);
        commands+=Operation(14,2,15);
        commands+=Operation(15,2,16);
        commands+=Operation(16,-1,2);
        //
    }
    fun UpperCommain(){
        for (i in commands.indices)
        {
            commands[i].number=i;
        }
    }
    fun UpdateLine(){
        selectInex=lineSize/2;
        line= listOf<Section>();
        commands= mutableListOf<Operation>();
        for (i in -(lineSize/2)..lineSize/2){
            line+=Section(i);
        }
        TestComands()
    }

    fun CommandAplly(index:Int){
        var commandsWrap:LinearLayout=findViewById(R.id.commandWrap);
        if(index!=0)
            (commandsWrap.getChildAt(index)as RelativeLayout).getChildAt(3).visibility=View.VISIBLE;
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
        if(commands[lastCommandId].comand==4)
        {
//            if(!line[lastCommandId].checked)
//            {
//                lastCommandId=commands[lastCommandId].link2!!;
//            }
//            else
//                lastCommandId=commands[lastCommandId].link!!;
        }
    }

    fun DrawCommand(){
        var commandsWrap:LinearLayout=findViewById(R.id.commandWrap);
        commandsWrap.removeAllViews();
        for (i  in -1..commands.indices.last){
            val newRelativeLayout = layoutInflater.inflate(R.layout.command_crad, null) as RelativeLayout
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0,0,0,0);
            newRelativeLayout.layoutParams=params;
            if(i==-1){
                ((newRelativeLayout.getChildAt(0)as TextView)).text="№";
                ((newRelativeLayout.getChildAt(1)as TextView)).text="Операция";
                ((newRelativeLayout.getChildAt(2)as TextView)).text="Ссылка";
                commandsWrap.addView(newRelativeLayout)
                newRelativeLayout.setId(i);
                continue;
            }

            ((newRelativeLayout.getChildAt(0)as TextView)).text=commands[i].number.toString();
            ((newRelativeLayout.getChildAt(1)as TextView)).text=commands[i].comand.toString();
            ((newRelativeLayout.getChildAt(2)as TextView)).text=commands[i].link.toString();
            commandsWrap.addView(newRelativeLayout)
            newRelativeLayout.setId(i);
            newRelativeLayout.setOnClickListener{
                commands.removeAt(i);
                UpperCommain();
                DrawCommand()
                deleteCount++;
            }
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
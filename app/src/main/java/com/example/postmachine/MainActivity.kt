package com.example.postmachine

import android.content.Intent
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
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
        scroll=findViewById(R.id.scroll);
        selectInex=lineSize/2;
        line= listOf<Section>();
        commands= mutableListOf<Operation>();
        for (i in -(lineSize/2)..lineSize/2){
            line+=Section(i);
        }
        TestComands()

        scroll.post {
            val centerX = scroll.getChildAt(0).width / 2
            scroll.smoothScrollBy(centerX-this.resources.displayMetrics.widthPixels/2,0);
        }
        AddListeners()
        DrawCommand()

    }
    fun AddListeners(){
        val tv:TextView=findViewById(R.id.textView2);
        layoutContainer=findViewById(R.id.sectionsWrap);
        DrawLine()
        findViewById<ImageButton>(R.id.playBtn).setOnClickListener{
            if(commands.size==0)
                return@setOnClickListener
            ClearOk();
            stopCheck=false;
            lastCommandId=0;
            var runnable:Runnable =Runnable{
                try {
                    tv.post{
                        findViewById<ImageButton>(R.id.playBtn).isEnabled=false;
                    }
                    while (commands[lastCommandId].comand!=-1) {
                        tv.post {
                            stopCheck=false;
                            if( lastCommandId in commands.indices)
                            {
                                if(commands[lastCommandId].link!=null)
                                {
                                    CommandAplly(lastCommandId);
                                    DrawLine()
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
                }
                catch (e:InterruptedException)
                {}
            };
            thread=Thread(runnable)
            thread.start();

        }
        findViewById<ImageView>(R.id.imageView4).setOnClickListener{
            scroll.post {

                    val centerX = scroll.getChildAt(0).width / 2
                    scroll.smoothScrollTo(centerX-this.resources.displayMetrics.widthPixels/2,0);

            }
        }
        findViewById<ImageView>(R.id.imageView).setOnClickListener{
            scroll.post {

                val centerX = scroll.getChildAt(0).width / 2
                scroll.smoothScrollTo(0,0);

            }
        }
        findViewById<ImageView>(R.id.imageView5).setOnClickListener{
            scroll.post {

                val centerX = scroll.getChildAt(0).width / 2
                scroll.smoothScrollTo(centerX*2,0);

            }
        }
        findViewById<ImageButton>(R.id.imageButton5).setOnClickListener{
            var intent:Intent= Intent(this@MainActivity,About::class.java);
            startActivity(intent);
        }
        findViewById<ImageButton>(R.id.refreshBtn).setOnClickListener{
                stopCheck = true;
                lastCommandId = 0;
                selectInex = lineSize / 2;
                for (i in line.indices)
                    line[i].checked = false;
                DrawLine()
                Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT).show()
                commands = mutableListOf<Operation>();
                DrawCommand();

        }

        findViewById<ImageButton>(R.id.nextStep).setOnClickListener{
            stopCheck=false;
            if(commands.size==0|| stopCheck)
                return@setOnClickListener;
            if( lastCommandId in 0..commands.indices.last+1)
            {
                if(commands[lastCommandId].link!=null)
                {
                    CommandAplly(lastCommandId);
                    DrawLine()
                }
                else
                {
                    lastCommandId=0;
                    Toast.makeText(this, "Не заданы команды", Toast.LENGTH_SHORT).show()
                    stopCheck=true;
                }
            }
            else
            {

                lastCommandId=-1;
            }

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
    fun ClearOk(){
        for(index in 0..commands.indices.last+1)
        {
            var commandsWrap:LinearLayout=findViewById(R.id.commandWrap);
            (commandsWrap.getChildAt(index)as RelativeLayout).getChildAt(3).visibility=View.INVISIBLE;
        }
    }
    fun TestComands(){
        /*
       Commands
        */
        commands+=Operation(0,4,1);
        commands[commands.size-1].link2=3;
        commands+=Operation(1,2,2);
        commands+=Operation(2,0,0);
        commands+=Operation(3,-1,0);
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
        (commandsWrap.getChildAt(index+1)as RelativeLayout).getChildAt(3).visibility=View.VISIBLE;
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
                selectInex = 0;
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
            ClearOk()
        }
        if(commands[lastCommandId].comand!=4)
        {
            val link =commands[lastCommandId].link
            lastCommandId= link!!-deleteCount;
        }
        else
        {
            if(line[selectInex!!].checked)
            {
                val link =commands[lastCommandId].link2
                lastCommandId= link!!-deleteCount;
            }
            else
            {
                val link =commands[lastCommandId].link
                lastCommandId= link!!-deleteCount;
            }
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
            if(commands[i].comand==-1)
                ((newRelativeLayout.getChildAt(1)as TextView)).text="!"
            if(commands[i].comand==0)
                ((newRelativeLayout.getChildAt(1)as TextView)).text="←"
            if(commands[i].comand==1)
                ((newRelativeLayout.getChildAt(1)as TextView)).text="→"
            if(commands[i].comand==2)
                ((newRelativeLayout.getChildAt(1)as TextView)).text="V"
            if(commands[i].comand==3)
                ((newRelativeLayout.getChildAt(1)as TextView)).text="X"
            if(commands[i].comand==4)
                ((newRelativeLayout.getChildAt(1)as TextView)).text="?"
            if(commands[i].comand!=4)
                ((newRelativeLayout.getChildAt(2)as TextView)).text=commands[i].link.toString();
            else
                ((newRelativeLayout.getChildAt(2)as TextView)).text=commands[i].link.toString()+" "+commands[i].link2.toString();
            commandsWrap.addView(newRelativeLayout)
            newRelativeLayout.setId(i);
        }
        val newRelativeLayout = layoutInflater.inflate(R.layout.card_add, null) as RelativeLayout
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0,0,0,0);
        newRelativeLayout.layoutParams=params;
        ((newRelativeLayout.getChildAt(3)as Button)).setOnClickListener{
            var arg:Int=0;

            if(((newRelativeLayout.getChildAt(1)as EditText)).text.length!=0)
            {
                arg=((newRelativeLayout.getChildAt(1)as EditText)).text.toString().toInt();
                if(((newRelativeLayout.getChildAt(0)as Spinner)).selectedItemId.toInt() ==0)
                {
                    commands+=Operation(commands.size,-1,arg);
                    DrawCommand();
                }
                if(((newRelativeLayout.getChildAt(0)as Spinner)).selectedItemId.toInt() ==1)
                {
                    commands+=Operation(commands.size,0,arg);
                    DrawCommand();
                }
                if(((newRelativeLayout.getChildAt(0)as Spinner)).selectedItemId.toInt() ==2)
                {
                    commands+=Operation(commands.size,1,arg);
                    DrawCommand();
                }
                if(((newRelativeLayout.getChildAt(0)as Spinner)).selectedItemId.toInt() ==3)
                {
                    commands+=Operation(commands.size,2,arg);
                    DrawCommand();
                }
                if(((newRelativeLayout.getChildAt(0)as Spinner)).selectedItemId.toInt() ==4)
                {
                    commands+=Operation(commands.size,3,arg);
                    DrawCommand();
                }
                if(((newRelativeLayout.getChildAt(0)as Spinner)).selectedItemId.toInt() ==5)
                {
                    ((newRelativeLayout.getChildAt(2)as EditText)).isEnabled=true;
                    if(((newRelativeLayout.getChildAt(2)as EditText)).text.length!=0){
                        commands+=Operation(commands.size,4,arg);
                        commands[commands.size-1].link2=((newRelativeLayout.getChildAt(2)as EditText)).text.toString().toInt();
                        commands[commands.indices.last].link2=((newRelativeLayout.getChildAt(2)as EditText)).text.toString().toInt();
                        DrawCommand();
                    }

                }

            }
        }
        commandsWrap.addView(newRelativeLayout);
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
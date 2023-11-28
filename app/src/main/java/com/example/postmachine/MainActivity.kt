package com.example.postmachine


import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size


class MainActivity : AppCompatActivity() {

    private var line= mutableListOf<Section>()
    private var lineSize=100
    private var carriage:Carriage= Carriage(lineSize/2)
    private var commands:Commands= Commands()
    private var operationCarrige:OperationCarrige=OperationCarrige(-1)
    private var lastCommandComplete=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_PostMachine)
        lineInit()
        addTestCommands()
        carriageUpdate()
        navigationCarriageListeners()
        navigationBottomButtonsListeners()
        setDefaulScroll()
        commandsIniit()
        operationCarriageUpdate()
        addListeners()
    }

    private fun applyCommands(){
        val layoutContainer=findViewById<LinearLayout>(R.id.sectionsWrap)
        operationCarrige.setNewLocation(lastCommandComplete)
        if(commands.getCommands()[lastCommandComplete].getOperationId()==0&&(carriage.getCarriageLocation())!=0)
            carriage.carriageLeft()

        if(commands.getCommands()[lastCommandComplete].getOperationId()==10&&(carriage.getCarriageLocation())!=lineSize/2)
            carriage.carriageRight()

        if(commands.getCommands()[lastCommandComplete].getOperationId()==2)
        {
            line[carriage.getCarriageLocation()].setCheck()
            ((layoutContainer.getChildAt(carriage.getCarriageLocation())as RelativeLayout).getChildAt(1) as RelativeLayout).getChildAt(0).visibility=View.VISIBLE
        }

        if(commands.getCommands()[lastCommandComplete].getOperationId()==3)
        {
            line[carriage.getCarriageLocation()].setUncheck()
            ((layoutContainer.getChildAt(carriage.getCarriageLocation())as RelativeLayout).getChildAt(1) as RelativeLayout).getChildAt(0).visibility=View.INVISIBLE
        }

        if(commands.getCommands()[lastCommandComplete].getOperationId()==3)
        {
            line[carriage.getCarriageLocation()].setUncheck()
            ((layoutContainer.getChildAt(carriage.getCarriageLocation())as RelativeLayout).getChildAt(1) as RelativeLayout).getChildAt(0).visibility=View.INVISIBLE
        }

        if(commands.getCommands()[lastCommandComplete].getOperationId()==3)
        {
            line[carriage.getCarriageLocation()].setUncheck()
            ((layoutContainer.getChildAt(carriage.getCarriageLocation())as RelativeLayout).getChildAt(1) as RelativeLayout).getChildAt(0).visibility=View.INVISIBLE
        }
        if(lastCommandComplete in 0..<commands.getCommands().indices.last &&commands.getCommands()[lastCommandComplete].getOperationId()!=5&&commands.getCommands()[lastCommandComplete].getOperationId()!=4)
            lastCommandComplete=commands.getCommands()[lastCommandComplete].getLink()

        else
        {
            if(commands.getCommands()[lastCommandComplete].getOperationId()!=4)
            {
                lastCommandComplete=0
                Toast.makeText(this, "Complete", Toast.LENGTH_SHORT).show()
            }
            else
            {
                lastCommandComplete = if(!line[carriage.getCarriageLocation()].getChecked())
                    commands.getCommands()[lastCommandComplete].getLink()
                else
                    commands.getCommands()[lastCommandComplete].getLink2()
            }
        }
        carriageUpdate()
        operationCarriageUpdate()
    }

    @SuppressLint("InflateParams")
    fun lineInit(){
        val layoutContainer=findViewById<LinearLayout>(R.id.sectionsWrap)
        layoutContainer.removeAllViews()
        for (i in -(lineSize/2)..lineSize/2)
            line+=Section(i)
        for (i in 0..lineSize) {
            val newRelativeLayout =
                layoutInflater.inflate(R.layout.section_card, null) as RelativeLayout
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 5, 0)

            newRelativeLayout.layoutParams = params
            ((newRelativeLayout.getChildAt(0) as TextView)).text = line[i].getIndex().toString()

            ((newRelativeLayout.getChildAt(1) as RelativeLayout)).setOnClickListener {
                line[i].check()
                if(line[i].getChecked())
                {
                    (newRelativeLayout.getChildAt(1)as RelativeLayout).getChildAt(0).visibility=View.VISIBLE
                }
                else
                {
                    (newRelativeLayout.getChildAt(1)as RelativeLayout).getChildAt(0).visibility=View.INVISIBLE
                }
            }
            if(line[i].getChecked())
                (newRelativeLayout.getChildAt(1)as RelativeLayout).getChildAt(0).visibility=View.VISIBLE
            layoutContainer.addView(newRelativeLayout)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    fun commandsIniit(){
        val layoutContainer=findViewById<LinearLayout>(R.id.commandWrap);
        layoutContainer.removeAllViews()
        for (i in -1..commands.getCommands().indices.last) {
            if (i==-1)
            {
                val newRelativeLayout =
                    layoutInflater.inflate(R.layout.command_crad_header, null) as LinearLayout
                val params = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 0)
                newRelativeLayout.layoutParams = params
                layoutContainer.addView(newRelativeLayout)
                continue
            }
            val newRelativeLayout =
                layoutInflater.inflate(R.layout.command_crad, null) as LinearLayout
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 0)
            newRelativeLayout.layoutParams = params
            (((newRelativeLayout.getChildAt(0) as LinearLayout).getChildAt(1) as TextView)).text = i.toString();
            (((newRelativeLayout.getChildAt(0) as LinearLayout).getChildAt(3) as TextView)).text = commands.getCommands().get(i).getOperationIcon()
            if(commands.getCommands().get(i).getOperationId()!=4) {
                (((newRelativeLayout.getChildAt(0) as LinearLayout).getChildAt(5) as TextView)).text = commands.getCommands().get(i).getLink().toString()
            } else {
                (((newRelativeLayout.getChildAt(0) as LinearLayout).getChildAt(5) as TextView)).text = commands.getCommands().get(i).getLink().toString()+" "+commands.getCommands().get(i).getLink2().toString()
            }
            layoutContainer.addView(newRelativeLayout)
        }
    }

    private fun addTestCommands(){
        commands.addCommand(4,1,2)
        commands.addCommand(2,0)
        commands.addCommand(0,3)
        commands.addCommand(5,0)
    }

    private fun refreshLine(){
        val layoutContainer=findViewById<LinearLayout>(R.id.sectionsWrap)
        if(line.size==0)
            return
        for (i in line.indices) {
            line[i].setUncheck()
            ((layoutContainer.getChildAt(i)as RelativeLayout).getChildAt(1) as RelativeLayout).getChildAt(0).visibility=View.INVISIBLE
        }
        if(carriage.getCarriageLocation()!=lineSize/2)
        {
            carriage.setNewLocation(lineSize/2)
            carriageUpdate()
        }
        lastCommandComplete=0
        operationCarrige.setNewLocation(-1);
        operationCarriageUpdate()
    }

    private fun carriageUpdate(){
        val layoutContainer=findViewById<LinearLayout>(R.id.sectionsWrap)

        ((layoutContainer.getChildAt(carriage.getCarriageLocation())as RelativeLayout).getChildAt(2)as RelativeLayout).visibility=View.VISIBLE

        if(carriage.getLastCarriageLocation().size!=0)
            for (i in carriage.getLastCarriageLocation())
                ((layoutContainer.getChildAt(i)as RelativeLayout).getChildAt(2)as RelativeLayout).visibility=View.INVISIBLE
        carriage.clearCarrigeList()
    }

    private fun addListeners() {
        carriage.setNewLocation(lineSize/2)
        findViewById<Button>(R.id.backBtn).setOnClickListener() {
            findViewById<ConstraintLayout>(R.id.addWrap).visibility = View.GONE;
        }
        findViewById<ImageButton>(R.id.aboutBtn).setOnClickListener{
            val intent:Intent= Intent(this@MainActivity,About::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.createBtn).setOnClickListener() {
            findViewById<ConstraintLayout>(R.id.addWrap).visibility = View.GONE;
            if (findViewById<EditText>(R.id.editTextNumber2).visibility == View.INVISIBLE) {
                if (findViewById<EditText>(R.id.editTextNumber).text.toString() != "")
                {
                    commands.addCommand(findViewById<Spinner>(R.id.spinner).selectedItemPosition, findViewById<EditText>(R.id.editTextNumber).text.toString().toInt())
                    commandsIniit()

                }
            }
            if (findViewById<EditText>(R.id.editTextNumber).text.toString() != "" && findViewById<EditText>(R.id.editTextNumber2).text.toString() != "")
            {
                commands.addCommand(findViewById<Spinner>(R.id.spinner).selectedItemPosition, findViewById<EditText>(R.id.editTextNumber).text.toString().toInt(), findViewById<EditText>(R.id.editTextNumber2).text.toString().toInt())
                commandsIniit()
            }
        }
        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            findViewById<EditText>(R.id.editTextNumber2).visibility =
                                View.INVISIBLE;findViewById<TextView>(R.id.textView9).visibility =
                                View.INVISIBLE
                        }

                        1 -> {
                            findViewById<EditText>(R.id.editTextNumber2).visibility =
                                View.INVISIBLE;findViewById<TextView>(R.id.textView9).visibility =
                                View.INVISIBLE
                        }

                        2 -> {
                            findViewById<EditText>(R.id.editTextNumber2).visibility =
                                View.INVISIBLE;findViewById<TextView>(R.id.textView9).visibility =
                                View.INVISIBLE
                        }

                        3 -> {
                            findViewById<EditText>(R.id.editTextNumber2).visibility =
                                View.INVISIBLE;findViewById<TextView>(R.id.textView9).visibility =
                                View.INVISIBLE
                        }

                        4 -> {
                            findViewById<EditText>(R.id.editTextNumber2).visibility =
                                View.VISIBLE;findViewById<TextView>(R.id.textView9).visibility =
                                View.VISIBLE
                        }

                        5 -> {
                            findViewById<EditText>(R.id.editTextNumber2).visibility =
                                View.INVISIBLE;findViewById<TextView>(R.id.textView9).visibility =
                                View.INVISIBLE
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        findViewById<Spinner>(R.id.spinner).onItemSelectedListener = itemSelectedListener
    }

    private fun operationCarriageUpdate(){
        val layoutContainer=findViewById<LinearLayout>(R.id.commandWrap)
        if (operationCarrige.getCarriageLocation()!=-1)
            ((layoutContainer.getChildAt(operationCarrige.getCarriageLocation()+1)as LinearLayout).getChildAt(0) as LinearLayout).getChildAt(0).visibility=View.VISIBLE

        if(operationCarrige.getLastCarriageLocation().size!=0)
            for (i in operationCarrige.getLastCarriageLocation())
                ((layoutContainer.getChildAt(i+1)as LinearLayout).getChildAt(0) as LinearLayout).getChildAt(0).visibility=View.INVISIBLE
        operationCarrige.clearCarrigeList()
    }
    private fun playCommands() {
        val layoutContainer=findViewById<LinearLayout>(R.id.commandWrap)
        val runnable:Runnable=Runnable{
            while(commands.getCommands()[lastCommandComplete].getOperationId()!=5) {
                layoutContainer.post {

                    applyCommands()
                }
                Thread.sleep(1000);
            }
        }

        var thread:Thread= Thread(runnable);
        thread.start();
    }

    private fun navigationBottomButtonsListeners(){
        findViewById<ImageButton>(R.id.nextStep).setOnClickListener{
            if (commands.getCommands().size!=0)
                applyCommands()
        }
        findViewById<ImageButton>(R.id.refreshBtn).setOnClickListener{

            refreshLine()
        }
        findViewById<ImageButton>(R.id.playBtn).setOnClickListener{
            if (commands.getCommands().size!=0)
                playCommands()
        }
        findViewById<ImageButton>(R.id.minusBtn).setOnClickListener{
            if(commands.getCommands().size!=0)
            {
                commands.removeLastCommand()
                val layoutContainer=findViewById<LinearLayout>(R.id.commandWrap)
                layoutContainer.removeViewAt(layoutContainer.size-1);
                carriage.setNewLocation(lineSize/2);
                lastCommandComplete=0;

            }
        }
        findViewById<ImageButton>(R.id.plusBtn).setOnClickListener{
            commandsIniit()
            findViewById<ConstraintLayout>(R.id.addWrap).visibility=View.VISIBLE;
        }
    }

    private fun setDefaulScroll(){
        val scroll:HorizontalScrollView=findViewById(R.id.scroll)
        scroll.post {
            val centerX = scroll.getChildAt(0).width / 2
            scroll.smoothScrollTo(centerX-this.resources.displayMetrics.widthPixels/2,0)
        }

    }

    private fun navigationCarriageListeners(){
        findViewById<ImageView>(R.id.imageView4).setOnClickListener{
            setDefaulScroll()
        }
        findViewById<ImageView>(R.id.imageView).setOnClickListener{
            val scroll:HorizontalScrollView=findViewById(R.id.scroll)
            scroll.post {
                scroll.smoothScrollTo(0,0)
            }
        }
        findViewById<ImageView>(R.id.imageView5).setOnClickListener{
            val scroll:HorizontalScrollView=findViewById(R.id.scroll)
            scroll.post {
                val centerX = scroll.getChildAt(0).width / 2
                scroll.smoothScrollTo(centerX*2,0)
            }
        }
    }
}
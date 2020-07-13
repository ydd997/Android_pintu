package com.example.pintu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton ib00,ib01,ib02,ib10,ib11,ib12,ib20,ib21,ib22;
    Button restartBtn;
    TextView timeTv;

    //每行图片的个数
    private int imageX=3;
    //每列图片的个数
    private int imageY=3;

    //图片的总个数
    private int imageCount=imageX*imageY;

    //空白区域的位置（是变化的，用一个变量来表示）
    private int blackSwap=imageCount-1; //默认是最后一个

    //初始化空白区域的按钮id
    private int blackImgid=R.id.pt_ib_02x02; //默认是最后一个

    //定义计数时间的变量
    int time=0;

    //存放碎片的数组，便于统一管理
    private int[]image={R.mipmap.img_00x00,R.mipmap.img_00x01,R.mipmap.img_00x02,
            R.mipmap.img_01x00,R.mipmap.img_01x01,R.mipmap.img_01x02,
            R.mipmap.img_02x00,R.mipmap.img_02x01,R.mipmap.img_02x02};

    //声明一个图片数组的下标数组，随机排列这个数组
    private int[]imageIndex=new int[image.length];

    //消息通信机制，重写handleMessag方法用来接收消息，形成时间累计的效果
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                time++;
                timeTv.setText("时间："+time+"秒");
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //打乱碎片的函数
        disruptRandom();

        //一进来每隔1s就发一条空消息出去，接收到这个空消息并让TextView发生改变，形成计数器的效果
        //延迟1s发送一条空消息：发送消息的编号是1，延迟时间为1000ms=1s
        handler.sendEmptyMessageDelayed(1,1000);
    }

    //随机打乱数组中的元素，以不规则的形式进行图片显示
    private void disruptRandom() {
        for (int i = 0; i < imageIndex.length; i++) {
            imageIndex[i]=i;
        }
        //规定20次，随机选择两个角标对应的值进行交换
        int rand1,rand2;
        for (int j = 0; j < 20; j++) {
            //随机生成第一个角标，生成0—8之间的随机数
            rand1=(int)(Math.random()*(imageIndex.length-1)); //Math.random()生成的是0—1之间的随机数，再乘以最大值减去最小值（即8-0），最后整体加上最小值0
            //第二次随机生成的角标不能和第一次相同，如果相同就不方便交换
            do {
                rand2=(int)(Math.random()*(imageIndex.length-1));
                if(rand1!=rand2)
                    break;
            }while (true);
            //交换数组两个角标上对应的值
            swap(rand1,rand2);
        }

        //随机排列到指定的控件上
        ib00.setImageResource(image[imageIndex[0]]);
        ib01.setImageResource(image[imageIndex[1]]);
        ib02.setImageResource(image[imageIndex[2]]);
        ib10.setImageResource(image[imageIndex[3]]);
        ib11.setImageResource(image[imageIndex[4]]);
        ib12.setImageResource(image[imageIndex[5]]);
        ib20.setImageResource(image[imageIndex[6]]);
        ib21.setImageResource(image[imageIndex[7]]);
        ib22.setImageResource(image[imageIndex[8]]);
    }

    //交换数组指定角标的数据
    private void swap(int rand1, int rand2) {
        int temp=imageIndex[rand1];
        imageIndex[rand1]=imageIndex[rand2];
        imageIndex[rand2]=temp;
    }

    /*初始化控件*/
    private void initView() {
        ib00=findViewById(R.id.pt_ib_00x00);
        ib01=findViewById(R.id.pt_ib_00x01);
        ib02=findViewById(R.id.pt_ib_00x02);
        ib10=findViewById(R.id.pt_ib_01x00);
        ib11=findViewById(R.id.pt_ib_01x01);
        ib12=findViewById(R.id.pt_ib_01x02);
        ib20=findViewById(R.id.pt_ib_02x00);
        ib21=findViewById(R.id.pt_ib_02x01);
        ib22=findViewById(R.id.pt_ib_02x02);
        restartBtn=findViewById(R.id.pt_btn_restart);
        timeTv=findViewById(R.id.pt_tv_time);
    }


    public void onClick(View view) {
        int id = view.getId();
        //九个按钮执行点击事件的逻辑是相同的：如果有空格在周围则可以改变图片的位置，否则点击事件不响应
        switch (id){
            case R.id.pt_ib_00x00:
                move(R.id.pt_ib_00x00,0);
                break;
            case R.id.pt_ib_00x01:
                move(R.id.pt_ib_00x01,1);
                break;
            case R.id.pt_ib_00x02:
                move(R.id.pt_ib_00x02,2);
                break;
            case R.id.pt_ib_01x00:
                move(R.id.pt_ib_01x00,3);
                break;
            case R.id.pt_ib_01x01:
                move(R.id.pt_ib_01x01,4);
                break;
            case R.id.pt_ib_01x02:
                move(R.id.pt_ib_01x02,5);
                break;
            case R.id.pt_ib_02x00:
                move(R.id.pt_ib_02x00,6);
                break;
            case R.id.pt_ib_02x01:
                move(R.id.pt_ib_02x01,7);
                break;
            case R.id.pt_ib_02x02:
                move(R.id.pt_ib_02x02,8);
                break;
        }
    }

    /*表示移动指定位置的按钮的函数：将图片和空白区域进行交换*/
    private void move(int imageButtonId, int site) {

        //判断选中的图片在第几行，取整来判断
        int sitex=site / imageX;

        //判断选中的图片在第几列，趋于来判断
        int sitey=site % imageY;

        //获取空白区域的坐标
        int blackx=blackSwap / imageX;
        int blacky=blackSwap % imageY;

        //可以移动的条件
        //1.在同一行，列数相减绝对值为1，可以移动；2.在同一列，行数相减绝对值为1，可以移动。
        int x=Math.abs(sitex-blackx);
        int y=Math.abs(sitey-blacky);
        if((x==0&&y==1)||(x==1&&y==0)){
            //通过id查找到这个可以移动的按钮
            ImageButton clickButton=findViewById(imageButtonId);
            //该可移动按钮不在显示图片
            clickButton.setVisibility(View.INVISIBLE);
            //查找空白区域的按钮
            ImageButton blackButton=findViewById(blackImgid);
            //将空白按钮设置为显示图片
            blackButton.setImageResource(image[imageIndex[site]]);
            //移动之前是不可见的，移动之后将控件设置为可见
            blackButton.setVisibility(View.VISIBLE);

            //上面的交换并没有存在数组之中，要调用swap函数，将改变角标的过程记录在存储图片位置的数组当中
            swap(site,blackSwap);

            //新的空白区域位置更新
            blackSwap=site;
            blackImgid=imageButtonId;
        }
        //判断本次移动后是否完成拼图游戏
        judgeGameOver();
    }

    /*判断拼图是否成功*/
    private void judgeGameOver() {
        boolean loop=true; //定义标志位
        //对存放图片角标的数组imageIndex进行判断
        for (int i = 0; i < imageIndex.length; i++) {
            if (imageIndex[i]!=i) {
                loop=false;
                break;
            }
        }
        if (loop) {
            //拼图成功
            //停止计时
            handler.removeMessages(1); //移除消息
            //禁止玩家继续移动按钮
            ib00.setClickable(false);
            ib01.setClickable(false);
            ib02.setClickable(false);
            ib10.setClickable(false);
            ib11.setClickable(false);
            ib12.setClickable(false);
            ib20.setClickable(false);
            ib21.setClickable(false);
            ib22.setClickable(false);
            //还原被点击图片按钮，还原成初始化的样子

            //显示之前隐藏的拼图
            ib22.setImageResource(image[8]);
            ib22.setVisibility(View.VISIBLE);
            //弹出对话框
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("拼图成功啦！您用的时间是"+time+"秒！")
                    .setPositiveButton("确认",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /*重新开始按钮的点击事件*/
    public void restart(View view) {
        //将状态还原
        restore();

        //将拼图重新打乱
        disruptRandom();

        //中断之前的消息
        handler.removeMessages(1);
        //将时间重新归零并且重新开始计时
        time=0;
        timeTv.setText("时间："+time+"秒");
        handler.sendEmptyMessageDelayed(1,1000);
    }

    private void restore() {
        //拼图游戏重新开始，允许玩家重新触碰按钮
        ib00.setClickable(true);
        ib01.setClickable(true);
        ib02.setClickable(true);
        ib10.setClickable(true);
        ib11.setClickable(true);
        ib12.setClickable(true);
        ib20.setClickable(true);
        ib21.setClickable(true);
        ib22.setClickable(true);

        //还原被点击的图片按钮变成初始化的模样
        //最后一次选中的空白区域显示出来
        ImageButton clickButton=findViewById(blackImgid);
        clickButton.setVisibility(View.VISIBLE);

        //定义一个新的图片按钮，设置为第九个，让其隐藏（默认隐藏第九张图片）
        ImageButton blackBtn=findViewById(R.id.pt_ib_02x02);
        blackBtn.setVisibility(View.INVISIBLE);

        //初始化空白区域的按钮id
        blackImgid=R.id.pt_ib_02x02;
        blackSwap=imageCount-1;
    }
}

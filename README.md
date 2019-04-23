
## 可以扩展的日期控件

> *  人民币符号暂时显示有问题，后期会调整
> *  会有点小bug
> *  数据暂为临时的Rondom数据，根据需求改动；
> *  DPMonthView类 ：init()方法中 
>   >     mLiveRadius = DensityUtils.dp2px(getContext(), 3);  
>   >     mPaddingBottom = DensityUtils.dp2px(getContext(), 12);
>   > 	  mTxtRadius = DensityUtils.dp2px(getContext(), 18);
> *  设置具体某一天与下属文字的距离;（具体调整效果自行调试运行查看）

> *  interface DPBaseDimen 定义对应各种效果

>  >public interface DPBaseDimen {
    
    	/**
    	 * 星期标题的高度
    	 *
    	 * @return
    	 */
    	int getTitleHeight();
    
    	/**
    	 * 星期标题的字体大小
    	 *
    	 * @return
    	 */
    	int getTitleTxtSize();
    
    	/**
    	 * 内容字体大小
    	 *
    	 * @return
    	 */
    	int getContentTxtSize();
    
    
    	/**
    	 * 价格字体大小
    	 *
    	 * @return
    	 */
    	int getPicTextSize();
>  >



==================
### 效果图展示

##Screenshots 
=================

![Image text](https://raw.githubusercontent.com/KungFuteddy/CalendarView/master/ScreenShot.png)

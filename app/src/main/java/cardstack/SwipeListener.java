package cardstack;

import android.animation.Animator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;

import com.mooviest.ui.SingletonSwipe;

/**
 * Created by aaron on 4/12/2015.
 */
public class SwipeListener implements View.OnTouchListener {

    private float ROTATION_DEGREES = 15f;
    float OPACITY_END = 0.33f;
    private float initialX;
    private float initialY;

    private int mActivePointerId;
    private float initialXPress;
    private float initialYPress;
    private ViewGroup parent;
    private float parentWidth;
    private float parentHeight;
    private int paddingLeft;
    private int paddingTop;

    private View card;
    SwipeCallback callback;
    private boolean deactivated;
    private View rightView;
    private View leftView;
    private View upView;
    private View downView;


    public SwipeListener(View card, final SwipeCallback callback, float initialX, float initialY, float rotation, float opacityEnd) {
        this.card = card;
        this.initialX = initialX;
        this.initialY = initialY;
        this.callback = callback;
        this.parent = (ViewGroup) card.getParent();
        this.parentWidth = parent.getWidth();
        this.parentHeight = parent.getHeight();
        this.ROTATION_DEGREES = rotation;
        this.OPACITY_END = opacityEnd;
        this.paddingLeft = ((ViewGroup) card.getParent()).getPaddingLeft();
        this.paddingTop = ((ViewGroup) card.getParent()).getPaddingTop();
    }

    public SwipeListener(View card, final SwipeCallback callback, float initialX, float initialY, float rotation, float opacityEnd, int screenWidth) {
        this.card = card;
        this.initialX = initialX;
        this.initialY = initialY;
        this.callback = callback;
        this.parent = (ViewGroup) card.getParent();
        this.parentWidth = screenWidth;
        this.ROTATION_DEGREES = rotation;
        this.OPACITY_END = opacityEnd;
        this.paddingLeft = ((ViewGroup) card.getParent()).getPaddingLeft();
        this.paddingTop = ((ViewGroup) card.getParent()).getPaddingTop();
    }


    private boolean click = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //DISABLE VIEWPAGER SWIPE
        SingletonSwipe.getInstance().enabled=false;

        if (deactivated) return false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                click = true;
                //gesture has begun
                float x;
                float y;
                //cancel any current animations
                v.clearAnimation();

                mActivePointerId = event.getPointerId(0);

                x = event.getX();
                y = event.getY();

                if(event.findPointerIndex(mActivePointerId) == 0) {
                    callback.cardActionDown();
                }

                initialXPress = x;
                initialYPress = y;
                break;

            case MotionEvent.ACTION_MOVE:
                //gesture is in progress

                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                //Log.i("pointer index: " , Integer.toString(pointerIndex));
                if(pointerIndex < 0 || pointerIndex > 0 ){
                    break;
                }

                final float xMove = event.getX(pointerIndex);
                final float yMove = event.getY(pointerIndex);

                //calculate distance moved
                final float dx = xMove - initialXPress;
                final float dy = yMove - initialYPress;

                //throw away the move in this case as it seems to be wrong
                //TODO: figure out why this is the case
                if((int)initialXPress == 0 && (int) initialYPress == 0){
                    //makes sure the pointer is valid
                    break;
                }
                //calc rotation here
                float posX = card.getX() + dx;
                float posY = card.getY() + dy;

                //in this circumstance consider the motion a click
                if (Math.abs(dx + dy) > 5) click = false;

                card.setX(posX);
                card.setY(posY);

                //Log.i("Positionx","X:"+posX);

                //Log.i("Positionx","Y:"+posY);

                //card.setRotation
                float distobjectX = posX - initialX;
                float rotation = ROTATION_DEGREES * 2.f * distobjectX / parentWidth;
                card.setRotation(rotation);

                Log.d("POS X",posX+"");
                Log.d("POS Y",posY+"");
                Log.d("paddingLeft",paddingLeft+"");
                Log.d("paddingTOP",paddingTop+"");


                if (rightView != null && leftView != null){
                    //set alpha of left and right image
                    float alpha = (((posX - paddingLeft) / (parentWidth * OPACITY_END)));
                    //float alpha = (((posX - paddingLeft) / parentWidth) * ALPHA_MAGNITUDE );
                    //Log.i("alpha: ", Float.toString(alpha));
                    rightView.setAlpha(alpha);
                    leftView.setAlpha(-alpha);
                }

                if(upView != null && leftView !=null){
                    //set alpha of up and down image
                    float alpha = (((posY - paddingTop) / (parentWidth * OPACITY_END)));
                    //float alpha = (((posX - paddingLeft) / parentWidth) * ALPHA_MAGNITUDE );
                    //Log.i("alpha: ", Float.toString(alpha));
                    upView.setAlpha(-alpha);
                    downView.setAlpha(alpha);
                }

                break;

            case MotionEvent.ACTION_UP:
                //gesture has finished
                //check to see if card has moved beyond the left or right bounds or reset
                //card position
                checkCardForEvent();

                if(event.findPointerIndex(mActivePointerId) == 0) {
                    callback.cardActionUp();
                }
                //check if this is a click event and then perform a click
                //this is a workaround, android doesn't play well with multiple listeners

                if (click) v.performClick();
                //if(click) return false;

                rightView.setAlpha(0);
                leftView.setAlpha(0);
                upView.setAlpha(0);
                downView.setAlpha(0);

                break;

            default:
                return false;
        }
        return true;
    }

    public void checkCardForEvent() {

        if (cardBeyondLeftBorder()) {
            animateOffScreenLeft(160)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            callback.cardOffScreen();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            callback.cardSwipedLeft();
            this.deactivated = true;
        } else if (cardBeyondRightBorder()) {
            animateOffScreenRight(160)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            callback.cardOffScreen();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            callback.cardSwipedRight();
            this.deactivated = true;
        } else if (cardBeyondUpBorder()) {
            animateOffScreenUp(160)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            callback.cardOffScreen();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            callback.cardSwipedUp();
            this.deactivated = true;
        }else if (cardBeyondDownBorder()) {
            animateOffScreenDown(160)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            callback.cardOffScreen();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            callback.cardSwipedDown();
            this.deactivated = true;
        } else {
            resetCardPosition();
        }
    }

    private boolean cardBeyondLeftBorder() {
        //check if cards middle is beyond the left quarter of the screen
        return (card.getX() + (card.getWidth() / 2) < (parentWidth / 4.f));
    }

    private boolean cardBeyondRightBorder() {
        //check if card middle is beyond the right quarter of the screen
        return (card.getX() + (card.getWidth() / 2) > ((parentWidth / 4.f) * 3));
    }

    private boolean cardBeyondUpBorder() {
        //check if card middle is beyond the top quarter of the screen
        return (card.getY() + (card.getHeight() / 2) < ((parentHeight / 4.f )));
    }

    private boolean cardBeyondDownBorder() {
        //check if card middle is beyond the top quarter of the screen

        //Log.i("Position Y","Y:"+card.getY());

        //Log.i("getHeight","H:"+(card.getHeight() / 2));

        //Log.i("ParentHeight","up:"+((parentHeight)));
        return (card.getY() + (card.getHeight() / 2) > ((parentHeight /1.5f)));
    }

    private ViewPropertyAnimator resetCardPosition() {
        if(rightView!=null)rightView.setAlpha(0);
        if(leftView!=null)leftView.setAlpha(0);
        Log.i("SwipeListener","Reset card position");
        // ENABLE VIEWPAGER SWIPE
        SingletonSwipe.getInstance().enabled=true;
        return card.animate()
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .x(initialX)
                .y(initialY)
                .rotation(0);
    }

    public ViewPropertyAnimator animateOffScreenLeft(int duration) {
        //Log.i("AnimateUP","up:"+(-parentWidth));
        leftView.setAlpha(1);

        //Log.i("Positionx","X:"+card.getX());

        //Log.i("Positionx","Y:"+card.getY());
        return card.animate()
                .setDuration(duration)
                .x(-(parentWidth))
                .y(0)
                .rotation(-30);
    }


    public ViewPropertyAnimator animateOffScreenRight(int duration) {
        //Log.i("AnimateUP","up:"+(parentWidth * 2));
        rightView.setAlpha(1);

        //Log.i("Positionx","X:"+card.getX());

        //Log.i("Positionx","Y:"+card.getY());
        return card.animate()
                .setDuration(duration)
                .x(parentWidth * 2)
                .y(0)
                .rotation(30);
    }

    public ViewPropertyAnimator animateOffScreenUp(int duration) {
        //Log.i("AnimateUP","up:"+(-(parentHeight)));
        upView.setAlpha(1);

        //Log.i("Positionx","X:"+card.getX());

        //Log.i("Positionx","Y:"+card.getY());
        return card.animate()
                .setDuration(duration)
                .x(initialX)
                .y(-(parentHeight))
                .rotation(0);
    }

    public ViewPropertyAnimator animateOffScreenDown(int duration) {
        //Log.i("AnimateUP","up:"+((parentHeight)));
        downView.setAlpha(1);

        //Log.i("Positionx","X:"+card.getX());

        //Log.i("Positionx","Y:"+card.getY());
        return card.animate()
                .setDuration(duration)
                .x(initialX)
                .y((parentHeight))
                .rotation(0);
    }

    public ViewPropertyAnimator animateOffScreen(int duration) {
        return card.animate()
                .setDuration(duration)
                .x(initialX)
                .y(initialY)
                .rotation(0);
    }


    public void setRightView(View image) {
        this.rightView = image;
    }

    public void setLeftView(View image) {
        this.leftView = image;
    }

    public void setUpView(View image) {
        this.upView = image;
    }

    public void setDownView(View image) {
        this.downView = image;
    }

    public interface SwipeCallback {
        void cardSwipedLeft();
        void cardSwipedRight();
        void cardSwipedUp();
        void cardSwipedDown();
        void cardOffScreen();
        void cardActionDown();
        void cardActionUp();
    }
}

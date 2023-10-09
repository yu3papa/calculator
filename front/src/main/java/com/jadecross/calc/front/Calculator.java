package com.jadecross.calc.front;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 계산로직참고 : https://smhope.tistory.com/204
 */
@RestController
public class Calculator {
    // 호출예) http://localhost:8080/calculate
    // expression -> 9+8-7*6/5
    @GetMapping ("/calculate")
    public float calculate(@RequestParam(value = "disp") String expression) {
        float result = calcExpression(organize(expression));
        System.out.printf("%s = %f\n", expression, result);
        return result;
    }

    // 계산 Operator(+,-,*,/) REST API 호출
    private float callCalcRestApi(String operator, float num1, float num2) {
        String endPoint = "http://calc-" + operator + "/?num1=" + num1 + "&num2=" + num2;
        RestTemplate restTemplate = new RestTemplate();
        Float apiResult = restTemplate.getForObject(endPoint, Float.class);

        return apiResult.floatValue();
    }

    public String[] organize(String s) {
        boolean isBracket = s.charAt(0) == '(';

        s = s.replace("(", " ( ");
        s = s.replace(")", " ) ");
        s = s.replace("+", " + ");
        s = s.replace("-", " - ");
        s = s.replace("/", " / ");
        s = s.replace("*", " * ");
        s = s.replace("  ", " ");

        String[] str = s.split(" ");

        ArrayList<String> sb = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (int i = 0; i < str.length; i++) {
            String now = str[i];

            switch (now){
                case "+":
                case "-":
                case "*":
                case "/":
                    while (!stack.isEmpty() && priority(stack.peek()) >= priority(now)) {
                        sb.add(stack.pop());
                    }
                    stack.push(now);
                    break;
                case "(":
                    stack.push(now);
                    break;
                case ")":
                    while(!stack.isEmpty() && !stack.peek().equals("(")){
                        sb.add(stack.pop());
                    }
                    stack.pop();
                    break;
                default:
                    sb.add(now);
            }
        }

        while (!stack.isEmpty()) {
            sb.add(stack.pop());
        }

        if(isBracket) {
            sb.remove(0);}

        String[] result = new String[sb.size()];

        for(int i = 0; i < sb.size(); i++) {
            result[i]=sb.get(i);
        }

        return result;
    }

    public int priority(String operator){

        if(operator.equals("(") || operator.equals(")")){
            return 0;
        } else if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }
        return -1;
    }

    public float calcExpression(String[] str) {

        Stack<Float> stack = new Stack<>();

        for (String x : str) {

            if (!x.equals("+")&&!x.equals("-")&&!x.equals("*")&&!x.equals("/")) {
                stack.push(Float.parseFloat(x));
            }else {

                float a = stack.pop();
                float b = stack.pop();

                switch (x) {
                    case "+":
                        // stack.push(b+a);
                        stack.push(callCalcRestApi("plus", b, a));
                        break;
                    case "-":
                        //stack.push(b-a);
                        stack.push(callCalcRestApi("minus", b, a));
                        break;
                    case "*":
                        //stack.push(b*a);
                        stack.push(callCalcRestApi("multiply", b, a));
                        break;
                    case "/":
                        //stack.push(b/a);
                        stack.push(callCalcRestApi("divide", b, a));
                        break;
                }
            }
        }
//        System.out.println(stack.pop());
        return stack.pop();
    }
}
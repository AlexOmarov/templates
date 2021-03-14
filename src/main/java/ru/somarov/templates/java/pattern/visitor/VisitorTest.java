package ru.somarov.templates.java.pattern.visitor;

class VisitorTest {


}

abstract class ExpressionVisitor
{
    abstract void visit(Expression exp);
}

abstract class Expression
{
    public void accept(ExpressionVisitor ev) {
        ev.visit(this);
    }
}

class Value extends Expression
{
    public int value;

    public Value(int value)
    {
        this.value = value;
    }

}

class AdditionExpression extends Expression
{
    public Expression lhs, rhs;

    public AdditionExpression(Expression lhs, Expression rhs)
    {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}

class MultiplicationExpression extends Expression
{
    public Expression lhs, rhs;

    public MultiplicationExpression(Expression lhs, Expression rhs)
    {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}

class ExpressionPrinter extends ExpressionVisitor
{
    private StringBuilder sb = new StringBuilder();

    @Override
    public String toString()
    {
        return sb.toString();
    }

    @Override
    void visit(Expression exp) {

        if(exp instanceof Value) {
            sb.append(((Value) exp).value);
        } else if(exp instanceof MultiplicationExpression) {
            sb.append("(");
            ((MultiplicationExpression) exp).lhs.accept(this);
            sb.append("*");
            ((MultiplicationExpression) exp).rhs.accept(this);
            sb.append(")");
        } else if(exp instanceof AdditionExpression) {
            sb.append("(");
            ((AdditionExpression) exp).lhs.accept(this);
            sb.append("+");
            ((AdditionExpression) exp).rhs.accept(this);
            sb.append(")");
        }
    }
}

class Demo {
    public static void main(String[] args) {
        AdditionExpression ae = new AdditionExpression(new Value(2), new Value(3));
        ExpressionPrinter exp = new ExpressionPrinter();
        exp.visit(ae);
        System.out.println(exp.toString());
    }
}

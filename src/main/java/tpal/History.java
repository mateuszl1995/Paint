package tpal;

import java.util.ArrayList;
import java.util.List;

import tpal.DrawArea.Step;

public class History {
	int counter = 0;

	DrawArea drawArea;
	List<Pair> steps;

	Paint paint;

	History(DrawArea drawArea) {
		this.drawArea = drawArea;
		steps = new ArrayList<Pair>();
	}

	public void undo() {
		if (counter == 0) return;
		if (front().getTool().equals("Release")) {
			counter--;
			while (counter > 0 && front().getTool().equals("Brush")) {
				counter--;
			}
		} else {
			counter--;
		}
		drawArea.clearGraphics();
		drawArea.setImage(drawArea.opened);
		for (int i = 0; i < counter; i++) {
			Pair p = steps.get(i);
			p.execute();
		}
		
		drawArea.repaint();
	}

	public void redo() {
		if (counter >= steps.size())
			return;
		
		Pair p = steps.get(counter++);
		p.execute();
		while (front().getTool().equals("Brush")) {
			p = steps.get(counter++);
			p.execute();
		}
		drawArea.repaint();
	}

	public void push(Object source, Object step) {
		steps = steps.subList(0, counter);
		steps.add(new Pair(source, step));
		counter++;
	}
	class Pair {
		Pair(Object source, Object step) { this.source = source; this.step = step; }
		Object source;
		Object step;
		public String getTool() {
			if (source.getClass() == DrawArea.class) {
				Step s = (Step) step;
				return s.tool;
			}
			return "";
		}
		public void execute() {
			if (source.getClass() == DrawArea.class) {
				((DrawArea) source).doStep(step);
			}
			else
				((Paint) paint).doStep(source, step);
		}
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	private Pair front() {
		return steps.get(counter-1);
	}
	
	public void clear() {
		steps.clear();
		counter = 0;
	}
}

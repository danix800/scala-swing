/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala.swing.uitest



import scala.swing.Swing._
import scala.swing.event.ButtonClicked
import scala.swing._


/**
 * Test for issue SI-7597 https://issues.scala-lang.org/browse/SI-7597
 * (expanded to include other showXXXDialog dialogs )
 */
object SI7597 extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "SI7597 showXXXDialog tests"
    size = new Dimension(900, 200)

    lazy val dialog = aDialog

    val fileChooserDialog = new FileChooser
    val colorChooser = new ColorChooser

    contents = new BoxPanel(Orientation.Vertical) {
      contents ++= Seq(
        fileChooserStyles("Component", parent = this),
        fileChooserStyles("Frame", parent = top),
        fileChooserStyles("Dialog", parent = dialog)
      )
    }

    def fileChooserStyles(rowTitle : String, parent : => PeerContainer) = new FlowPanel {
      contents ++= Seq(new Label(s"Parent is $rowTitle"))

      contents ++= Seq(
        simpleButton("Open", fileChooserDialog.showOpenDialog(parent)),
        simpleButton("Save", fileChooserDialog.showSaveDialog(parent)),
        simpleButton("Text", fileChooserDialog.showDialog(parent, "Text")),
        simpleButton("Confirmation", Dialog.showConfirmation(parent, "Confirmation") ),
        simpleButton("Input", Dialog.showInput(parent, "Input", initial = "Some text") ),
        simpleButton("Message", Dialog.showMessage(parent, "Message" )),
        simpleButton("Message", Dialog.showOptions(parent, "Message", entries = List("First", "Second", "Third"), initial=1 )),
        simpleButton("Color", ColorChooser.showDialog(parent, "Color", java.awt.Color.RED))
      )
    }

    def simpleButton(parentTitle : String, dialogChooser : => Any): Button = new Button {
      text = parentTitle
      reactions += {
        case _ : ButtonClicked =>
          dialogChooser match {
            case action => println(s"Result: $action")
          }
      }
    }
  }


  def aDialog:Dialog = new Dialog(top) {
    title = "A Dialog"
    size = new Dimension(300, 600)
    contents = new Label("Test Dialog.  Do Not Close")
    visible = true
  }
}
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.File

fun main() {
    val pDocument = PDDocument.load(File("input.pdf"))
    val acroForm = pDocument.documentCatalog.acroForm
    
    val field = acroForm.getField("I_AM_A_TEAPOT")
    val widget = field.widgets.first()
    val page = widget.page
    val rectangle = widget.rectangle
    val x = rectangle.upperRightX
    val y = rectangle.upperRightY
    val h = rectangle.height
    val w = rectangle.width
    
    // remove the widget
    val annotations = page.annotations.filter { annotation ->
        annotation.cosObject != widget.cosObject
    }
    page.annotations = annotations
    
    // add the string
    val contentStream = PDPageContentStream(pDocument, page, PDPageContentStream.AppendMode.APPEND, true)
    contentStream.beginText()
    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12f)
    contentStream.newLineAtOffset(x - w,y - h)
    contentStream.showText("/Initials/")
    contentStream.endText()
    contentStream.close()
    
    pDocument.save(File("output.pdf"))
    pDocument.close()
    
}
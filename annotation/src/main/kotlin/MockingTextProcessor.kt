import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate
import io.cursedfunction.annotation.mockme.MockingText
import java.io.OutputStream

// Extension function to append text to a file output stream
private fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

/**
 * A symbol processor that generates extension functions
 * for data classes annotated with @MockingText
 */
class MockingTextProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // Get all symbols with the MockingText annotation
        val symbols = resolver.getSymbolsWithAnnotation(MockingText::class.qualifiedName!!)

        // If there are symbols to process...
        if (symbols.iterator().hasNext()) {
            symbols
                // Filter out symbols that are not valid
                .filter { it.validate() }
                // Filter out symbols that are not class declarations
                .filter { it is KSClassDeclaration }
                // For each symbol, create a new file and write the generated code
                .forEach { symbol ->

                    // Create a new file to write the generated code
                    val file: OutputStream = codeGenerator.createNewFile(
                        dependencies = Dependencies(true),
                        packageName = symbol.containingFile!!.packageName.asString(),
                        fileName = "${symbol}Ext"
                    )

                    // Invoke the visitor to generate the code for
                    // each symbol that was annotated with @MockingText
                    symbol.accept(MockingTextVisitor(file), Unit)

                    // It is critically important to close the file after writing
                    file.close()
                }
        } else {
            // If there are no symbols to process, return an empty list
            return emptyList()
        }

        // Return the list of symbols that were not
        // processed, so they can be reprocessed
        return symbols.filterNot { it.validate() }.toList()
    }

    /**
     * A visitor that generates extension functions for data
     * classes annotated with the @MockingText annotation
     */
    inner class MockingTextVisitor(private val file: OutputStream) : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            // Visit the primary constructor of the class
            classDeclaration.primaryConstructor!!.accept(this, data)

            // Check if the class is a data class
            if (classDeclaration.modifiers.none { it == Modifier.DATA }) {
                logger.error("@MockingText must target a data class", classDeclaration)
            }

            // Get all properties of the class that are of
            // type String and not extension receivers
            val mockableParams = classDeclaration.getAllProperties()
                .filter { it.validate() }
                .filter { it.extensionReceiver == null }
                .filter { it.type.resolve().declaration.qualifiedName!!.asString() == String::class.qualifiedName!! }
                .toList()

            // If there are no parameters to make funny text, return
            if (mockableParams.isEmpty()) return

            // Write caution message to the file
            file.appendText(
                """
                /*
                 * CAUTION: This file is generated by the MockingTextProcessor
                 * Do not modify it manually as it may be overwritten
                 * by the processor on the next build
                 */


            """.trimIndent()
            )

            // Write the package name to the file
            val packageName = classDeclaration.packageName.asString()
            file.appendText("package $packageName\n\n")

            // Write the extension function to the file
            file.appendText(
                """
                fun ${classDeclaration.simpleName.asString()}.mockMe(): ${String::class.simpleName} {
                    val outStr = StringBuilder()

                    str.forEachIndexed { i, char ->
                        if (i % 2 == 0) {
                            outStr.append(char.uppercaseChar())
                        } else {
                            outStr.append(char.lowercaseChar())
                        }
                    }

                    return outStr.toString()
                }
            """.trimIndent()
            )
        }
    }
}

/**
 * A symbol processor provider that creates
 * instances of the MockingTextProcessor
 */
class MockingTextProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MockingTextProcessor(environment.codeGenerator, environment.logger)
    }
}
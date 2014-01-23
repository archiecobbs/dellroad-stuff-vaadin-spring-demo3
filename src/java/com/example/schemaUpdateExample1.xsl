<?xml version="1.0" encoding="UTF-8"?>

<!--
    Example of a schema update used by SpringPersistentObjectSchemaUpdater and SpringXSLUpdateTransformConfigurer
-->
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <!-- Example of renaming an XML element -->
    <xsl:template match="/Foobar/oldName">
        <newName>
            <xsl:apply-templates select="@*|node()"/>
        </newName>
    </xsl:template>

    <!-- Default action is to copy as-is -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:transform>

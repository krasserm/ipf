//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.09 at 11:50:13 AM PDT 
//


package org.openehealth.ipf.commons.ihe.xds.core.requests;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * Request object for a single Series.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Clay Sebourn
 */

@XmlRootElement(name = "RetrieveSeries")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveSeriesType", propOrder = {"seriesInstanceUID", "documents"})
public class RetrieveSeries
{

    @XmlAttribute(name = "seriesInstanceUID", required = true)
    protected String seriesInstanceUID;

    @XmlElementRef
    private List<RetrieveDocument> documents = new ArrayList<RetrieveDocument>();

    /**
     * Constructs the RetrieveSeries.
     */
    public RetrieveSeries() {}

    /**
     * Constructs the Series request.
     *
     * @param seriesInstanceUID    he series instance UID.
     * @param documents            the documents.
     */
    public RetrieveSeries(String seriesInstanceUID, List<RetrieveDocument> documents) {
        this.seriesInstanceUID = seriesInstanceUID;
        this.documents = documents;
    }

    /**
     * Gets the value of the seriesInstanceUID property.
     *
     * @return seriesInstanceUID      {@link String }
     *
     */
    public String getSeriesInstanceUID() {
        return seriesInstanceUID;
    }

    /**
     * @param seriesInstanceUID
     *          the unique ID of the series instance.
     */
    public void setSeriesInstanceUID(String seriesInstanceUID) {
        this.seriesInstanceUID = seriesInstanceUID;
    }

    /**
     * @return the list of documents to retrieve.
     */
    public List<RetrieveDocument> getDocuments() {
        return documents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((seriesInstanceUID == null) ? 0 : seriesInstanceUID.hashCode());
        result = prime * result + ((documents == null) ? 0 : documents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RetrieveSeries other = (RetrieveSeries) obj;

        if (seriesInstanceUID == null) {
            if (other.seriesInstanceUID != null)
                return false;
        } else if (!seriesInstanceUID.equals(other.seriesInstanceUID))
            return false;

        if (documents == null) {
            if (other.documents != null)
                return false;
        } else if (!documents.equals(other.documents))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

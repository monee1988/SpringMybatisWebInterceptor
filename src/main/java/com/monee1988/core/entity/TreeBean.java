package com.monee1988.core.entity;

import com.monee1988.core.util.Reflections;
import com.monee1988.core.util.StringUtils;

public abstract class TreeBean<T> extends BaseBean<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected T parent;

	protected String parentCodes;

	protected Integer sorts;

	protected String hasnext;

	protected Integer sortGrade;

	protected String pid;

	protected Boolean isQueryChildren;

	public static final String ROOT_CODE = "0";

	public TreeBean() {
		this.sorts = Integer.valueOf(30);
		this.isQueryChildren = Boolean.valueOf(true);
	}

	public TreeBean(String id) {
		super(id);
	}

	public abstract T getParent();

	public abstract void setParent(T paramT);

	public String getParentCode() {
		String id = null;
		if (this.parent != null) {
			id = (String) Reflections.invokeGetter(this.parent, "id");
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public void setParentCode(String parentCode) {
		if (this.parent == null) {
			try {
				this.parent = (T) getClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Reflections.invokeSetter(this.parent, "id", parentCode);
	}

	public String getParentCodes() {
		return this.parentCodes;
	}

	public void setParentCodes(String parentCodes) {
		this.parentCodes = parentCodes;
	}

	public Integer getSorts() {
		return this.sorts;
	}

	public void setSorts(Integer sorts) {
		this.sorts = sorts;
	}

	public String getHasnext() {
		return this.hasnext;
	}

	public void setHasnext(String hasnext) {
		this.hasnext = hasnext;
	}

	public Integer getSortGrade() {
		if ((this.hasnext != null) && (this.sortGrade == null)) {
			this.sortGrade = (this.parentCodes != null ? Integer
					.valueOf(this.parentCodes.replaceAll("[^,]", "").length() - 1)
					: null);
		}
		return this.sortGrade;
	}

	public void setSortGrade(Integer sortGrade) {
		this.sortGrade = sortGrade;
	}

	public Boolean getIsLeaf() {
		if (this.hasnext != null) {
			return Boolean.valueOf(!"1".equals(this.hasnext));
		}
		return null;
	}

	public String getPid() {
		if ((this.pid == null) || (StringUtils.isBlank(this.pid))) {
			String id = getParentCode();
			this.pid = (StringUtils.isNotBlank(id) ? id : "0");
		}
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public boolean getIsRoot() {
		return "0".equals(getPid());
	}

	public Boolean getIsQueryChildren() {
		return this.isQueryChildren;
	}

	public void setIsQueryChildren(Boolean isQueryChildren) {
		this.isQueryChildren = isQueryChildren;
	}
}

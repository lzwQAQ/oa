package com.kuyuner.core.config.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * k+开发框架所有的配置都在这个地方
 *
 * @author tangzj
 */
@ConfigurationProperties(prefix = KuyunerProperties.PAGER_PREFIX)
public class KuyunerProperties {
    static final String PAGER_PREFIX = "kuyuner";
    private ErrorPage errorPage = new ErrorPage();
    private Shiro shiro = new Shiro();

    private String fileBasePath;
    private String adminPath;
    private String ehcacheConfigFile;

    public ErrorPage getErrorPage() {
        return errorPage;
    }

    public Shiro getShiro() {
        return shiro;
    }

    public String getFileBasePath() {
        return fileBasePath;
    }

    public void setFileBasePath(String fileBasePath) {
        this.fileBasePath = fileBasePath;
    }

    public String getAdminPath() {
        return adminPath;
    }

    public void setAdminPath(String adminPath) {
        this.adminPath = adminPath;
    }

    public String getEhcacheConfigFile() {
        return ehcacheConfigFile;
    }

    public void setEhcacheConfigFile(String ehcacheConfigFile) {
        this.ehcacheConfigFile = ehcacheConfigFile;
    }

    public static class ErrorPage {
        private String page404;

        private String page403;

        private String page500;

        public String getPage404() {
            return page404;
        }

        public void setPage404(String page404) {
            this.page404 = page404;
        }

        public String getPage403() {
            return page403;
        }

        public void setPage403(String page403) {
            this.page403 = page403;
        }

        public String getPage500() {
            return page500;
        }

        public void setPage500(String page500) {
            this.page500 = page500;
        }
    }

    public static class Shiro {
        private String cipherKey;
        private boolean captcha;
        private String[] anons = {};

        public String getCipherKey() {
            return cipherKey;
        }

        public void setCipherKey(String cipherKey) {
            this.cipherKey = cipherKey;
        }

        public boolean isCaptcha() {
            return captcha;
        }

        public void setCaptcha(boolean captcha) {
            this.captcha = captcha;
        }

        public String[] getAnons() {
            return anons;
        }

        public void setAnons(String[] anons) {
            this.anons = anons;
        }
    }

}

import { IconPrefix, IconName, IconProp, IconLookup } from '@fortawesome/fontawesome-svg-core';

export class IconUtils {
    public static readonly DEFAULT_PREFIX: IconPrefix = 'fas';
    public static readonly DEFAULT_ICON_NAME: IconName = 'xmark';

    public static iconName(iconName: string): IconName {
        if (!iconName) iconName = IconUtils.DEFAULT_ICON_NAME;

        return iconName as IconName;
    }

    public static iconPrefix(prefix: string): IconPrefix {
        if (!prefix) prefix = IconUtils.DEFAULT_PREFIX;

        return prefix as IconPrefix;
    }

    public static iconProp(prefix: IconPrefix, iconName: IconName): IconProp {
        if (!prefix) prefix = IconUtils.DEFAULT_PREFIX;
        if (!iconName) iconName = IconUtils.DEFAULT_ICON_NAME;

        return [prefix, iconName];
    }

    public static iconLookup(prefix: IconPrefix, iconName: IconName): IconLookup {
        if (!prefix) prefix = IconUtils.DEFAULT_PREFIX;
        if (!iconName) iconName = IconUtils.DEFAULT_ICON_NAME;

        return {
            prefix,
            iconName,
        } as IconLookup;
    }

    public static iconString(prefix: IconPrefix, iconName: IconName): string {
        if (!prefix) prefix = IconUtils.DEFAULT_PREFIX;
        if (!iconName) iconName = IconUtils.DEFAULT_ICON_NAME;

        return `${prefix} ${iconName}`;
    }
}
